package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "수집 Data 등록 DTO")
@Data
public class CollectionDataCreateDto {
    @Schema(description = "수집 Data ID")
    private String collectionId;

    @Schema(description = "수집 Data 제목")
    private String title;

    @Schema(description = "사용자 ID")
    private String userId;
}
