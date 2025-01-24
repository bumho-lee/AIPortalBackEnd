package esea.esea_api.chat;

import lombok.extern.log4j.Log4j2;

import esea.esea_api.entities.Knowledge;
import esea.esea_api.enums.CONVERSATION_SOURCE_TYPE;
import esea.esea_api.entities.CollectionData;
import esea.esea_api.entities.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import esea.esea_api.chat.dto.ChatCreateRequestDto;
import esea.esea_api.chat.dto.ChatExampleResponseDto;
import esea.esea_api.chat.dto.ChatResponseDto;
import esea.esea_api.chat.dto.ConversationCompleteRequestDto;
import esea.esea_api.chat.dto.ReactionRequestDto;
import esea.esea_api.chat.dto.SourceResponseDto;
import esea.esea_api.chat.dto.ConversationReactionDto;

import esea.esea_api.entities.ConversationReaction;

import esea.esea_api.repositories.ConversationReactionRepository;
import esea.esea_api.repositories.ChatExampleRepository;
import esea.esea_api.repositories.KnowledgeRepository;
import esea.esea_api.repositories.CollectionRepository;
import esea.esea_api.util.SourcePath;

import esea.esea_api.repositories.CollectionDataRepository;

import esea.esea_api.dynamodb_repository.ChatHistoryRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import esea.esea_api.chat.dto.ConversationResponseDto;
import esea.esea_api.chat.dto.KnowledgeDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Log4j2
@Service
public class ChatService {
    @Autowired
    private SourcePath sourcePath;

    @Autowired
    private ConversationReactionRepository reactionRepository;

    @Autowired
    private ChatExampleRepository chatExampleRepository;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionDataRepository collectionDataRepository;

    // 채팅 예제 갖고오기
    public List<ChatExampleResponseDto> getChatExampleList() {
        return chatExampleRepository.findAll().stream()
                .map(ChatExampleResponseDto::new)
                .toList();
    }

    // 지식 갖고오기
    public Knowledge getKnowledge(Integer knowledgeId) {
        return knowledgeRepository.findById(knowledgeId).orElse(null);
    }

    // 지식 목록 갖고오기
    public List<KnowledgeDto> getKnowledgeList() {
        // knowledge를 active로 정렬해서 리스트 갖고오기
        List<Knowledge> knowledges = knowledgeRepository.findAllByOrderByActiveDesc();

        return knowledges.stream()
                .map(KnowledgeDto::new)
                .toList();
    }

    // 채팅 생성 후 채팅 아이디 반환
    @Transactional
    public String createChat(ChatCreateRequestDto body) {
        String SK = java.util.UUID.randomUUID().toString();
        String chatId = body.getUserId() + "-" + SK;

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PK", new AttributeValue().withS(body.getUserId()));
        item.put("SK", new AttributeValue().withS(chatId));
        item.put("create_time", new AttributeValue().withS(
                OffsetDateTime.now()
                        .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        item.put("llm_model", new AttributeValue().withS(body.getModel()));

        chatHistoryRepository.save(item);

        return chatId;
        // 다이나모 DB 생성
    }

    // 채팅 갖고오기
    public Map<String, Object> getChat(String pk, String sk) {
        return chatHistoryRepository.findByPKAndSK(pk, sk);
    }

    // 채팅 삭제
    @Transactional
    public void deleteChat(String pk, String sk) {
        chatHistoryRepository.deleteByPKAndSK(pk, sk);
        reactionRepository.deleteByConversationId(sk);
    }

    // 채팅 목록 갖고오기
    public List<ChatResponseDto> getChatList(String userId) {
        List<Map<String, Object>> chatList = chatHistoryRepository.findSortedChatHistory(userId);

        return chatList.stream()
                .map(chat -> new ChatResponseDto(chat, userId))
                .toList();
    }

    // 대화 생성 후 대화 아이디 반환
    @Transactional
    public String createConversation(Map<String, Object> chat, String message) {
        if (chat.get("title") == null || chat.get("title").equals("")) {
            chatHistoryRepository.updateChatTitle(chat.get("PK").toString(), chat.get("SK").toString(), message);
        }

        String message_id = "message-" + java.util.UUID.randomUUID().toString();

        return message_id;
    }

    // 대화 목록 갖고오기
    public List<ConversationResponseDto> getConversationList(String pk, String sk) {
        Map<String, Object> chat = chatHistoryRepository.findByPKAndSK(pk, sk);
        
        if (chat == null) {
            return List.of();
        }

        if (chat.get("message") == null || ((List<?>) chat.get("message")).isEmpty()) {
            return List.of();
        }

        // DynamoDB에서 가져온 메시지 리스트를 List<Map>으로 캐스팅
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> messageList = (List<Map<String, Object>>) chat.get("message");

        // 메시지 리스트를 ConversationResponseDto로 변환
        List<ConversationResponseDto> conversationList = messageList.stream()
            .map(message -> {
                ConversationResponseDto dto = new ConversationResponseDto(message, sk);

                // 지식 정보
                @SuppressWarnings("unchecked")
                List<String> indexes = message.get("category") != null ? 
                    (List<String>) message.get("category") : 
                    List.of();

                if(!indexes.isEmpty()) {
                    List<Knowledge> knowledges = new ArrayList<>();

                    indexes.forEach(index -> {
                        List<Knowledge> knowledgeRaws = knowledgeRepository.findAllByIndexCode(index);

                        if(!knowledgeRaws.isEmpty()) {
                            knowledges.addAll(knowledgeRaws);
                        }
                    });

                    dto.setKnowledgeIds(knowledges.stream()
                    .map(Knowledge::getKnowledgeId)
                    .toList());
                }
                
                // 출처 정보 전처리
                @SuppressWarnings("unchecked")
                List<String> sources = message.get("source") != null ?
                    (List<String>) message.get("source") : 
                    List.of();

                sources.forEach(source -> {
                    SourceResponseDto sourceResponseDto = extractDocumentName(source);
                    if(sourceResponseDto != null) {
                        // 중복 체크
                        long duplicateCount = dto.getSources().stream()
                            .filter(existingSource -> existingSource.getTitle().equals(sourceResponseDto.getTitle())).count();

                        if(duplicateCount == 0) {
                            dto.getSources().add(sourceResponseDto);
                        }
                    }
                });

                // 출처 중복 처리
                try {
                    List<SourceResponseDto> duplicateSources = isDuplicateSource(dto.getSources());
                    dto.setSources(duplicateSources);    
                } catch (Exception e) {
                    log.error("출처 중복 처리 오류", e);
                }

                // 리액션
                ConversationReaction reaction = reactionRepository.findByConversationId(dto.getConversationId());
                if (reaction != null) {
                    dto.setConversationReaction(new ConversationReactionDto(reaction));
                }

                // 지식 정보 중복 처리
                if(!dto.getKnowledgeIds().isEmpty()) {
                    List<Integer> uniqueKnowledgeIds = dto.getKnowledgeIds().stream()
                        .distinct()
                        .toList();
                    dto.setKnowledgeIds(uniqueKnowledgeIds);
                }

                return dto;
            })
            .toList();

        // 대화 반응 추가
        return conversationList;
    }

    // 대화 완료
    @Transactional
    public List<SourceResponseDto> completeConversation(ConversationCompleteRequestDto body) {
        List<SourceResponseDto> result = new ArrayList<>();

        // ArrayList로 변경하여 수정 가능한 리스트 생성
        Map<String, Object> chat = chatHistoryRepository.findByPKAndSK(body.getUserId(), body.getChatId());

        // 메세지 리스트
        Object messageObj = chat.get("message");
        if(messageObj == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "존재하지 않는 대화입니다.");
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> messageList = (List<Map<String, Object>>) messageObj;

        // 대화 아이디로 대화 찾기
        Map<String, Object> message = messageList.stream()
                .filter(item -> item.get("message_id").equals(body.getConversationId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "존재하지 않는 대화입니다."));

        @SuppressWarnings("unchecked")
        List<String> sources = message.get("source") != null ? 
            (List<String>) message.get("source") : 
            List.of();

        sources.forEach(source -> {
            SourceResponseDto sourceResponseDto = extractDocumentName(source);

            // 중복 체크
            if(sourceResponseDto != null) {
                if(result.stream()
                    .filter(existingSource -> existingSource.getTitle().equals(sourceResponseDto.getTitle())).count() == 0) 
                {
                    result.add(sourceResponseDto);
                }
            }
        });

        return isDuplicateSource(result);
    }

    // 대화 리액션 생성
    @Transactional
    public void createReaction(ReactionRequestDto data) {
        // 기존 리액션 찾기
        ConversationReaction existingReaction = reactionRepository.findByConversationId(data.getConversationId());
        if (existingReaction != null) {
            // 기존 리액션이 있으면 업데이트
            existingReaction.setConversationId(data.getConversationId());
            existingReaction.setType(data.getType());
            existingReaction.setCategory(data.getCategory());
            existingReaction.setReason(data.getReason());
            reactionRepository.save(existingReaction);
        } else {
            // 새로운 리액션 생성
            ConversationReaction reaction = new ConversationReaction();
            reaction.setConversationId(data.getConversationId());
            reaction.setType(data.getType());
            reaction.setCategory(data.getCategory());
            reaction.setReason(data.getReason());
            reactionRepository.save(reaction);
        }
    }

    // 대화 리액션 취소
    @Transactional
    public void cancelReaction(String conversationId) {
        ConversationReaction reaction = reactionRepository.findByConversationId(conversationId);

        if (reaction != null) {
            reactionRepository.delete(reaction);
        }
    }

    // 다이나모DB 채팅 하나만 갖고오기
    public Map<String, Object> getChatHistoryById() {
        return chatHistoryRepository.findByPKAndSK("id003", "id003-chat125");
    }

    // 다이나모DB 채팅 불러오기
    public List<Map<String, Object>> getChatHistory() {
        return chatHistoryRepository.findAll();
    }

    // 문서 제목에서 이름 추출
    private SourceResponseDto extractDocumentName(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }

        String regex = "\\['([^']+)'](?: \\(Page ([^)]+)\\))?(?: \\['([^']+)'])?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(title);

        if (matcher.find()) {
            String path = matcher.group(1);
            String page = matcher.group(2) != null ? matcher.group(2) : "없음";
            String indexCode = matcher.group(3) != null ? matcher.group(3) : "없음";

            String sourceType = null;

            // 컬렉션 조회
            Collection collection = collectionRepository.findByIndexCode(indexCode);
            if(collection != null) {
                sourceType = collection.getSourceType();
            } else {
                sourceType = "INDEX_FILE_PATH";
            }


            CollectionData collectionData = null;

            if(sourceType != null) {
                switch (sourceType) {
                case "NAME":
                    collectionData = collectionDataRepository.findLatestByNameIgnoreCase(path);
                    break;
                case "FILE_NAME":
                    collectionData = collectionDataRepository.findLatestByFileNameIgnoreCase(path);
                    break;
                case "INDEX_FILE_PATH":
                    collectionData = collectionDataRepository.findLatestByIndexFilePathIgnoreCase(path);
                    break;
                default:
                    collectionData = null;
                }  
            }

            if(collectionData != null && collection != null) {
                SourceResponseDto result = new SourceResponseDto(collectionData, collection.getSourceType(), sourcePath);

                // page 추가
                if (result.getType() == CONVERSATION_SOURCE_TYPE.PDF && page != "없음") {
                    result.setTitle(result.getTitle() + " (Page" + page + ")");
                }

                Collection parentCollection = collectionRepository.findByCollectionId(collectionData.getCollectionId());

                // 지식 아이디 설정
                List<Knowledge> knowledgeRaws = knowledgeRepository.findAllByIndexCode(parentCollection.getIndexCode());
                if(!knowledgeRaws.isEmpty()) {
                    result.setKnowledgeId(knowledgeRaws.get(0).getKnowledgeId());
                }

                return result;
            }
        }

        return null;
    }

    private List<SourceResponseDto> isDuplicateSource(List<SourceResponseDto> sources) {
        Map<String, List<SourceResponseDto>> nameGroups = new HashMap<>();
    
        // 이름별로 그룹화
        for (SourceResponseDto source : sources) {
            nameGroups.computeIfAbsent(source.getTitle(), k -> new ArrayList<>()).add(source);
        }
        
        // 중복된 이름이 있는 경우 처리
        for (List<SourceResponseDto> group : nameGroups.values()) {
            if (group.size() > 1) {
                // collectionDataId 기준으로 정렬
                group.sort((a, b) -> Integer.compare(a.getCollectionDataId(), b.getCollectionDataId()));
                
                // 번호 추가
                for (int i = 0; i < group.size(); i++) {
                    SourceResponseDto source = group.get(i);
                    source.setTitle(source.getTitle() + " (" + (i + 1) + ")");
                }
            }
        }
        
        return sources;
    }
}
