package esea.esea_api.statistics.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "사용자 LLM 사용량 응답 DTO")
@Data
public class UserLlmUsageResponseDto {
    @Schema(description = "사용자 LLM 사용량 목록")
    private List<DailyLlmUserUsageDto> content;

    private int totalCount;
}
