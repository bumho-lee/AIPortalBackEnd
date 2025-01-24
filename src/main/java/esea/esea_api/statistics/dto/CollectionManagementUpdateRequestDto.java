package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CollectionManagementUpdateRequestDto extends CollectionManagementRequestDto {
    @Schema(description = "수집 Data ID")
    private String collectionId;
}
