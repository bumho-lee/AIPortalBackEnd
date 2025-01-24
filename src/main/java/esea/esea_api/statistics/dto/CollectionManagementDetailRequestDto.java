package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CollectionManagementDetailRequestDto {
    @Schema(description = "수집 Data ID")
    private String collectionId;
}
