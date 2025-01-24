package esea.esea_api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum CONVERSATION_SOURCE_TYPE {
    @Schema(description = "파일")
    FILE, // 파일
    @Schema(description = "URL")
    URL, // URL
    @Schema(description = "PDF")
    PDF // URL
}
