package esea.esea_api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CONVERSATION_REACTION_TYPE {
    @Schema(description = "좋아요")
    LIKE, // 좋아요
    @Schema(description = "싫어요")
    DISLIKE, // 싫어요
    @Schema(description = "리액션 취소")
    NONE // 리액션 취소
}
