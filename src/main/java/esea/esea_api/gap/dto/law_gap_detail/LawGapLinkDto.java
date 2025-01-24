package esea.esea_api.gap.dto.law_gap_detail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "갭분석 법원 링크", example = "법원 링크")
@Data
public class LawGapLinkDto {
    @Schema(description = "링크", example = "링크")
    private String link;

    @Schema(description = "제목", example = "제목")
    private String title;
}
