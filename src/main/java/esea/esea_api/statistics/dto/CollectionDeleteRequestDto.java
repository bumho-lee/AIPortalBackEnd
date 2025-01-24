package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CollectionDeleteRequestDto {
    @Schema(description = "수집 Data ID")
    private List<Integer> ids;
}
