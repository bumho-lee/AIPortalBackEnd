package esea.esea_api.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
@Tag(name = "웹소켓 API", description = "웹소켓 연결 엔드포인트")
@RestController
@RequestMapping("/ws-docs")
public class WebSocketDocController {

    @Operation(summary = "LLM 채팅 웹소켓", description = "실제 LLM 연동 웹소켓")
    @GetMapping("/llmchat")
    public void wsDocsLLMChat(@RequestParam("userId") String userId, @RequestParam("chatId") String chatId, @RequestParam("conversationId") String conversationId, @RequestParam("message") String message, @RequestParam("knowledgeIds") List<String> knowledgeIds) {
        // 이 메서드는 실제로 호출되지 않으며, 문서화 목적으로만 존재합니다.
    }

    @Operation(summary = "LLM 채팅 웹소켓", description = "테스트용 LLM 연동 웹소켓")
    @GetMapping("/llmchat-test")
    public void wsDocsLLMChatTest(@RequestParam("userId") String userId, @RequestParam("chatId") String chatId, @RequestParam("conversationId") String conversationId, @RequestParam("message") String message, @RequestParam("knowledgeIds") List<String> knowledgeIds) {
        // 이 메서드는 실제로 호출되지 않으며, 문서화 목적으로만 존재합니다.
    }
}