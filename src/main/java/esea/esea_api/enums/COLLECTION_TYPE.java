package esea.esea_api.enums;

import io.swagger.v3.oas.annotations.media.Schema;

public enum COLLECTION_TYPE {
    @Schema(description = "외부 수집")
    EXTERNAL, // 외부 수집
    @Schema(description = "내부 수집")
    INTERNAL // 내부 수집
}
