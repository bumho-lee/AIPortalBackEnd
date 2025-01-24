package esea.esea_api.gap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "갭분석 피드백 목록 요청 DTO")
public class FeedbackListRequestDto {
    @Schema(description = "갭분석 ID")
    private String lawGapId;
}
