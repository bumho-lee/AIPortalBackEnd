package esea.esea_api.gap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LawGapCreateRequestDto {
    @Schema(description = "유저 아이디")
    private String userId;

    @Schema(description = "갭분석 아이디")
    private Integer gapId;
}
