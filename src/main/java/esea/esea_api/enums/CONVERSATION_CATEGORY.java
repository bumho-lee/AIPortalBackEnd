package esea.esea_api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CONVERSATION_CATEGORY {
    @Schema(description = "정확도")
    ACCURACY, // 정확도

    @Schema(description = "친절도")
    ANSWER_RELEVANCE, // 친절도

    @Schema(description = "속도")
    CONTEXTUAL_RELEVANCE, // 속도

    @Schema(description = "정확도")
    SECURITY // 정확도
}
