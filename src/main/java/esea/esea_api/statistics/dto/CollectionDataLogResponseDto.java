package esea.esea_api.statistics.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class CollectionDataLogResponseDto {
    @Schema(description = "수집 데이타 로그 목록")
    private List<CollectionDataResponseDto> content;

    @Schema(description = "총 개수")
    private Integer totalCount;

}
