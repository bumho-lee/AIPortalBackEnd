package esea.esea_api.gap.dto.law_gap_detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "갭분석 피드백")
@Data
public class LawGapFeedbackRequestDto {
    @Schema(description = "갭분석 아이디")
    private int gapId;

    @Schema(description = "유저 아이디")
    private String userId;

    @Schema(description = "피드백")
    private String feedback;

    @Schema(description = "comment", nullable = true, defaultValue = "")
    private String comment;
}
