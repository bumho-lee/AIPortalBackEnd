package esea.esea_api.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "대화 완료")
public class ConversationCompleteRequestDto {
    @Schema(description = "유저 아이디")
    private String userId;

    @Schema(description = "채팅 아이디")
    private String chatId;

    @Schema(description = "대화 아이디")
    private String conversationId;
}
