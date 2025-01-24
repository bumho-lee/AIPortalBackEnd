package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Schema(description = "수집 Data 관리 목록 요청 DTO")
@Data
public class CollectionListRequestDto {
    @Min(value = 1, message = "페이지는 1 이상이어야 합니다")
    @Schema(description = "페이지", example = "1", required = true, nullable = false, defaultValue = "1")
    private int page;

    @Min(value = 1, message = "페이지 사이즈는 1 이상이어야 합니다")
    @Schema(description = "페이지 사이즈", example = "25", required = true, nullable = false, defaultValue = "25")
    private int size;

    public int getPage() {
        if(page <= 0) {
            return 0;
        }

        return page - 1;
    }
}
