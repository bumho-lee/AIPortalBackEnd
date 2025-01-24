package esea.esea_api.statistics.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "수집 Data 관리 목록 응답 DTO")
@Data
public class CollectionListResponseDto {
    private List<CollectionResponseDto> content;
    private Integer totalCount;
}
