package esea.esea_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import esea.esea_api.chat.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import io.swagger.v3.oas.annotations.tags.Tag;

@Configuration
@EnableWebSocket
@Tag(name = "웹소켓 API", description = "웹소켓 연결 엔드포인트")
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler, "/llmchat", "/llmchat-test")
                .setAllowedOrigins("*");
    }
}
