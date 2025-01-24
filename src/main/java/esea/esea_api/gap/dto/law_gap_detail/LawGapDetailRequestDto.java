package esea.esea_api.gap.dto.law_gap_detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "갭분석 상세정보 요청 DTO")
@NoArgsConstructor
@Data
public class LawGapDetailRequestDto {
    @Schema(description = "유저 아이디", example = "1")
    private String userId;

    @Schema(description = "갭분석 아이디", example = "1")
    private Integer gapId;
}
