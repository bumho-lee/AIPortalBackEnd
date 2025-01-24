package esea.esea_api.chat.dto;

import java.util.List;

import java.util.ArrayList;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatResponseWrapperDto {
    @Schema(description = "LLM 모델 정보")
    private String model = "";

    @Schema(description = "Knowledge 지식 정보 코드값 목록")
    private List<Integer> knowledge = new ArrayList<>();

    @Schema(description = "채팅 아이디")
    private String chatId = "";

    @Schema(description = "마지막 대화 아이디", nullable = true)
    private String conversationId = "";

    @Schema(description = "대화 내용")
    private List<ConversationResponseDto> contents = new ArrayList<>();
}