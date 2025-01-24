package esea.esea_api.gap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "갭분석 관리자 요청 DTO")
public class LawGapManagerRequestDto {
    @Schema(description = "사용자 ID")
    private String userId;
}

