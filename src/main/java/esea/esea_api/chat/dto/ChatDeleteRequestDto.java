package esea.esea_api.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(description = "채팅 삭제 요청 DTO")
public class ChatDeleteRequestDto {
    @Schema(description = "채팅 아이디", example = "chat125")
    private String chatId;

    @Schema(description = "유저 아이디", example = "id001")
    private String userId;
}
