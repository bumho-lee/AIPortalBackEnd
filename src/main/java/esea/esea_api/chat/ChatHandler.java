package esea.esea_api.chat;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;

// ai socket용
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;

import esea.esea_api.entities.Knowledge;
import esea.esea_api.repositories.KnowledgeRepository;

@Component
public class ChatHandler extends TextWebSocketHandler {
    // 클라이언트 세션을 저장하는 해시맵
    private final ConcurrentHashMap<String, WebSocketSession> clientSessions = new ConcurrentHashMap<>();
    // AI 서버 세션을 저장하는 해시맵
    private final ConcurrentHashMap<String, WebSocketSession> aiServerSessions = new ConcurrentHashMap<>();
    // 이모지 상태를 저장하는 맵
    private final ConcurrentHashMap<String, Boolean> hasSeenFireEmoji = new ConcurrentHashMap<>();

    // AI 서버 웹소켓 URL
    private final String aiServerUrl = System.getenv("AI_SERVER_URL");

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    /**
     * 클라이언트로부터 받은 메시지를 처리하는 메소드
     * 
     * @param clientSession 클라이언트의 웹소켓 세션
     * @param message       클라이언트가 보낸 메시지
     * @throws Exception 메시지 처리 중 발생할 수 있는 예외
     */
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession clientSession, @NonNull TextMessage message)
            throws Exception {

        URI sessionUri = clientSession.getUri();
        String uri = sessionUri != null ? sessionUri.getPath() : "/llmchat-test";

        if ("/llmchat-test".equals(uri)) {
            // AI 연동 전 목업 작업
            handleClientMessage(clientSession, message);
        } else {
            // 실 작업
            // 클라이언트 세션 ID 가져오기
            String clientSessionId = clientSession.getId();
            // 해당 클라이언트와 연결된 AI 서버 세션 가져오기
            WebSocketSession aiServerSession = aiServerSessions.get(clientSessionId);

            // AI 서버 세션이 없거나 닫혀있으면 새로 연결
            if (aiServerSession == null || !aiServerSession.isOpen()) {
                aiServerSession = connectToOtherServer(clientSessionId).get();
            }

            // 메시지 페이로드 추출
            String payload = message.getPayload();
            // 클라이언트 메시지를 JSON으로 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode clientMessage = objectMapper.readTree(payload);

            // null 체크 추가
            JsonNode userIdNode = clientMessage.get("userId");
            if (userIdNode == null) {
                throw new IllegalArgumentException("userId is required");
            }
            String userId = userIdNode.asText();

            JsonNode chatIdNode = clientMessage.get("chatId");
            if (chatIdNode == null) {
                throw new IllegalArgumentException("chatId is required");
            }
            String chatId = chatIdNode.asText().replace(userId + "-", "");

            // knowledge 아이디 추출
            JsonNode knowledgeNode = clientMessage.get("knowledgeIds");
            List<Integer> knowledgeIds = new ArrayList<>();
            if (knowledgeNode != null && knowledgeNode.isArray()) {
                for (JsonNode node : knowledgeNode) {
                    knowledgeIds.add(node.asInt());
                }
            }

            // 카테고리 추출
            List<String> categories = new ArrayList<>();
            if (!knowledgeIds.isEmpty()) {
                List<Knowledge> knowledges = knowledgeRepository.findAllByKnowledgeIdIn(knowledgeIds);
                for (Knowledge knowledge : knowledges) {
                    List<String> codes = knowledge.getIndexCode();
                    categories.addAll(codes);
                }    
            }
            
            // AI 서버용 요청 메시지 생성
            var aiRequestMessage = new HashMap<String, Object>();
            aiRequestMessage.put("question", clientMessage.get("message").asText());

            // conversationId null 체크 추가
            JsonNode conversationIdNode = clientMessage.get("conversationId");
            if (conversationIdNode == null) {
                throw new IllegalArgumentException("conversationId is required");
            }
            aiRequestMessage.put("message_id", conversationIdNode.asText());

            aiRequestMessage.put("category", categories);
            aiRequestMessage.put("user_id", userId);
            aiRequestMessage.put("chat_id", chatId);
            aiRequestMessage.put("knowledge", !knowledgeIds.isEmpty());
            aiRequestMessage.put("gap_type", clientMessage.get("gapType") != null ? clientMessage.get("gapType").asBoolean() : false);

            // 고정값
            aiRequestMessage.put("reranker", false);
            aiRequestMessage.put("aggs", true);
            aiRequestMessage.put("k", 5);

            // JSON 문자열로 변환
            String aiMessageJson = objectMapper.writeValueAsString(aiRequestMessage);
            TextMessage textMessage = new TextMessage(aiMessageJson);

            // AI 서버로 메시지 전송
            aiServerSession.sendMessage(textMessage);
        }
    }

    /**
     * 클라이언트 메시지를 처리하고 응답을 보내는 메소드
     * 
     * @param clientSession 클라이언트의 웹소켓 세션
     * @param message       클라이언트가 보낸 메시지
     * @throws Exception 메시지 처리 중 발생할 수 있는 예외
     */
    private void handleClientMessage(WebSocketSession clientSession, TextMessage message) throws Exception {
        // 응답 메시지
        String respString = "";

        respString = MockChatResponses.CHAT_MOCK_RESPONSE;

        TextMessage responseMessage = new TextMessage(respString);

        clientSession.sendMessage(responseMessage);

        // 대화 종료
        clientSession.close();
    }

    // AI 서버에 연결하는 메소드
    private CompletableFuture<WebSocketSession> connectToOtherServer(String clientSessionId) {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        return webSocketClient.execute(new TextWebSocketHandler() {
            private final String aiClientSessionId = clientSessionId;  // 클래스 레벨 변수로 저장

            @Override
            public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message)
                    throws Exception {
                WebSocketSession clientSession = clientSessions.get(aiClientSessionId);
                if (clientSession != null && clientSession.isOpen()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(message.getPayload());

                    JsonNode chunkNode = jsonNode.get("chunk");
                    if (chunkNode != null) {
                        String answer = chunkNode.asText();
                        String sessionId = clientSession.getId();                        

                        if (!clientSession.isOpen()) {
                            return;
                        }
                        
                        // 이모지 상태 확인
                        if (hasSeenFireEmoji.getOrDefault(sessionId, false)) {
                            answer = "";
                            return;
                        }
                        
                        // 이모지 찾기
                        if (answer.contains("🔥")) {
                            String[] parts = answer.split("🔥");
                            answer = parts.length > 0 ? parts[0].trim() : "";

                            hasSeenFireEmoji.put(sessionId, true);
                        }

                        clientSession.sendMessage(new TextMessage(answer));
                    }
                }
            }

            @Override
            public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
                // AI 서버 연결이 종료되면 해당하는 클라이언트 연결도 종료
                WebSocketSession clientSession = clientSessions.get(aiClientSessionId);
                if (clientSession != null && clientSession.isOpen()) {
                    clientSession.close(status);
                }
                // 세션 정리
                aiServerSessions.remove(aiClientSessionId);
                clientSessions.remove(aiClientSessionId);
                hasSeenFireEmoji.remove(aiClientSessionId);
            }
        }, new WebSocketHttpHeaders(), URI.create(aiServerUrl))
        .thenApply(session -> {
            aiServerSessions.put(clientSessionId, session);
            return session;
        });
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        // 클라이언트 연결 성공 시 세션 저장
        clientSessions.put(session.getId(), session);
        System.out.println("연결 성공 : " + session.getId());
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        String sessionId = session.getId();
        
        // AI 서버 세션 가져오기
        WebSocketSession aiServerSession = aiServerSessions.get(sessionId);
        
        // AI 서버 세션이 존재하고 아직 열려있는 경우에만 종료
        if (aiServerSession != null && aiServerSession.isOpen()) {
            aiServerSession.close();
        }
        
        // 세션 정보들 제거
        clientSessions.remove(sessionId);
        aiServerSessions.remove(sessionId);
        hasSeenFireEmoji.remove(sessionId);
    }
}
