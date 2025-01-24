package esea.esea_api.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "채팅 생성 DTO")
public class ChatCreateRequestDto {
    @Schema(description = "유저 아이디", example = "user-1234567890", required = false, nullable = true)
    private String userId = "anonymous";

    @Schema(description = "채팅 LLM 모델", example = "bedrock", required = false, nullable = true, defaultValue = "bedrock")
    private String model = "bedrock";
}
