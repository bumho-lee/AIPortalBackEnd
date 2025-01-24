package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(description = "수집 Data 요청 DTO")
@Data
public class CollectionDataRequestDto {
    
    @Schema(description = "수집 Data ID")
    private String collectionId;
    
    @Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "시작일", example = "20240101", nullable = true, required = false)
    private String startDate;

    @Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "종료일", example = "20240101", nullable = true, required = false)
    private String endDate;

    @Schema(description = "검색어")
    private String keyword;

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

    public void setStartDate(String startDate) {
        this.startDate = (startDate != null && startDate.trim().isEmpty()) ? null : startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = (endDate != null && endDate.trim().isEmpty()) ? null : endDate;
    }

    public void setKeyword(String keyword) {
        this.keyword = (keyword != null && keyword.trim().isEmpty()) ? null : keyword;
    }
}
