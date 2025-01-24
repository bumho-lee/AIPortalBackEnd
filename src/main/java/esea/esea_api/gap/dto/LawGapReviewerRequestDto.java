package esea.esea_api.gap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "갭분석 검토자 선정 요청 DTO")
public class LawGapReviewerRequestDto {
    @Schema(description = "갭분석 ID", example = "1")
    private String gapId;

    @Schema(description = "유저 ID", example = "1")
    private String userId;
}
