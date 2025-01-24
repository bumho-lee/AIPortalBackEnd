package esea.esea_api.chat.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "대화 요청 DTO")
public class ConversationRequestDto {
    @Schema(description = "유저 ID", example = "1")
    private String userId;

    @Schema(description = "채팅 ID", example = "1")
    private String chatId;

    @Schema(description = "메시지 내용", example = "안녕하세요")
    private String message;
}
