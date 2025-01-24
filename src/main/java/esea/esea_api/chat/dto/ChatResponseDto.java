package esea.esea_api.chat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDto {
    private String chatId;
    private String title;
    private String userId;
    private String regDt;

    public ChatResponseDto(Map<String, Object> chat, String userId) {
        this.chatId = chat.get("SK").toString();
        this.title = chat.get("title") != null ? chat.get("title").toString() : "";
        this.regDt = chat.get("create_time").toString();
        this.userId = userId;
    }
}
