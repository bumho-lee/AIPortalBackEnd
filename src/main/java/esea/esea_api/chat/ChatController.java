package esea.esea_api.chat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.chat.dto.ConversationRequestDto;
import esea.esea_api.chat.dto.ConversationResponseDto;
import esea.esea_api.chat.dto.ReactionRequestDto;
import esea.esea_api.chat.dto.ChatCreateRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import esea.esea_api.chat.dto.ChatExampleResponseDto;
import esea.esea_api.chat.dto.ChatResponseDto;
import esea.esea_api.chat.dto.ChatResponseWrapperDto;
import esea.esea_api.chat.dto.ConversationCompleteDto;
import esea.esea_api.chat.dto.KnowledgeDto;
import esea.esea_api.chat.dto.SourceResponseDto;
import esea.esea_api.chat.dto.ChatDeleteRequestDto;
import esea.esea_api.chat.dto.ConversationCompleteRequestDto;

import esea.esea_api.enums.CONVERSATION_REACTION_TYPE;

import java.util.Map;
import java.util.List;

@Tag(name = "채팅 API", description = "채팅 Rest API 목록")
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Operation(summary = "채팅 예제 목록 갖고오기", description = "채팅 예제 목록 갖고오기")
    @GetMapping("/example")
    public List<ChatExampleResponseDto> getChatExampleList() {
        return chatService.getChatExampleList();
    }

    @Operation(summary = "지식 목록 갖고오기", description = "지식 목록 갖고오기")
    @GetMapping("/knowledge")
    public List<KnowledgeDto> getKnowledgeList() {
        return chatService.getKnowledgeList();
    }

    @Operation(summary = "채팅 생성", description = "채팅 생성 후 채팅 아이디 반환")
    @PostMapping()
    public Map<String, String> create(
            @RequestBody(required = false) ChatCreateRequestDto body) {
        if (body == null) {
            body = new ChatCreateRequestDto(); // 기본값이 설정된 새로운 객체 생성
        }

        // 채팅 생성
        String chatId = chatService.createChat(body);

        return Map.of("chatId", chatId);
    }

    @Operation(summary = "채팅 과거 내역 갖고오기", description = "사용자의 채팅 목록을 페이지네이션으로 갖고옵니다")
    @GetMapping()
    public List<ChatResponseDto> getChatList(@RequestParam(name = "userId") String userId) {
        return chatService.getChatList(userId);
    }

    @Operation(summary = "채팅 삭제", description = "채팅 삭제")
    @DeleteMapping()
    public void deleteChat(@RequestBody ChatDeleteRequestDto body) {
        chatService.deleteChat(body.getUserId(), body.getChatId());
    }

    @Operation(summary = "대화 생성", description = "대화 생성 후 대화 아이디 반환")
    @PostMapping("/conversation")
    public Map<String, String> createConversation(
            @Parameter(description = "메시지와 채팅 아이디") @RequestBody ConversationRequestDto body) {

        Map<String, Object> chat = chatService.getChat(body.getUserId(), body.getChatId());

        if (chat == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "존재하지 않는 채팅입니다.");
        }

        return Map.of("conversationId", chatService.createConversation(chat, body.getMessage()));
    }

    // 대화 목록 갖고오기
    @Operation(summary = "대화 목록 (채팅 상세 정보)", description = "대화 목록 (채팅 상세 정보)")
    @GetMapping("/conversations")
    public ChatResponseWrapperDto getConversationList(
            @Parameter(name = "chatId", description = "채팅 아이디") @RequestParam(name = "chatId") String chatId,
            @Parameter(name = "userId", description = "유저 아이디") @RequestParam(name = "userId") String userId) {

        // 채팅 정보 갖고오기
        Map<String, Object> chat = chatService.getChat(userId, chatId);
        
        // 응답 객체 생성
        ChatResponseWrapperDto result = new ChatResponseWrapperDto();
        if(chat == null) { 
            return result; 
        }

        result.setChatId(chatId);

        // 대화 목록
        List<ConversationResponseDto> contents = chatService.getConversationList(userId, chatId);
        result.setContents(contents);

        // 마지막 대화 정보 갖고오기
        if (!contents.isEmpty()) {
            ConversationResponseDto lastConversation = contents.get(contents.size() - 1);

            // 대화 아이디 설정
            result.setConversationId(lastConversation.getConversationId());

            // 대화에서 선택한 지식 정보
            result.setKnowledge(lastConversation.getKnowledgeIds());
        }

        // 대화 llm 갖고오기
        result.setModel(chat.get("llm_model").toString());

        return result;
    }

    // 대화 리액션
    @Operation(summary = "대화 리액션", description = "대화 리액션")
    @PostMapping("/reaction")
    public void createReaction(@RequestBody ReactionRequestDto body) {
        if (body.getType() == CONVERSATION_REACTION_TYPE.NONE) {
            chatService.cancelReaction(body.getConversationId());
        } else {
            chatService.createReaction(body);
        }

    }

    // 대화 완료
    @Operation(summary = "대화 완료", description = "대화 완료")
    @PostMapping("/complete")
    public ConversationCompleteDto completeConversation(@RequestBody ConversationCompleteRequestDto body) {
        // 출처 목록 갖고오기
        List<SourceResponseDto> sources = chatService.completeConversation(body);
        
        ConversationCompleteDto result = new ConversationCompleteDto(StringUtils.defaultIfEmpty(System.getenv("EXPERT_LINK"), "https://she.hanwha-total.net/SafetyQnA/Inquiry/Index"), sources);

        return result;
    }

    @Hidden
    @GetMapping("/historyOne")
    public Map<String, Object> getChatHistoryById() {
        return chatService.getChatHistoryById();
    }

    // 다이나모DB 채팅 불러오기
    @Hidden
    @GetMapping("/history")
    public List<Map<String, Object>> getChatHistory() {
        return chatService.getChatHistory();
    }
}

