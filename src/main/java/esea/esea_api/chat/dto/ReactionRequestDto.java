package esea.esea_api.chat.dto;

import esea.esea_api.enums.CONVERSATION_CATEGORY;
import esea.esea_api.enums.CONVERSATION_REACTION_TYPE;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "대화 리액션 요청 DTO")
public class ReactionRequestDto {
    @Schema(description = "대화 ID", example = "1")
    private String conversationId;

    @Schema(description = "리액션 타입 (LIKE, DISLIKE, NONE)", example = "LIKE")
    private CONVERSATION_REACTION_TYPE type;

    @Schema(description = "카테고리", example = "ACCURACY")
    private CONVERSATION_CATEGORY category = CONVERSATION_CATEGORY.ACCURACY;

    @Schema(description = "리액션 이유", example = "좋은 답변이에요")
    private String reason;
}
