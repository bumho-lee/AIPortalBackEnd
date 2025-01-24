package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "유저별 사용량 요청 DTO")
public class UserLlmUsageRequestDto {
    @Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "시작일", example = "20240101", nullable = true, required = false)
    private String startDate;

    @Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "종료일", example = "20240101", nullable = true, required = false)
    private String endDate;

    @Schema(description = "model 서비스 키", example = "bedrock", nullable = true, required = false)
    private String model;

    @Schema(description = "사용자 ID", example = "user123", nullable = true, required = false)
    private String userId;

    @Schema(description = "부서 ID", example = "deptId123", nullable = true, required = false)
    private String deptId;

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

    public void setModel(String model) {
        this.model = (model != null && model.trim().isEmpty()) ? null : model;
    }

    public void setUserId(String userId) {
        this.userId = (userId != null && userId.trim().isEmpty()) ? null : userId;
    }

    public void setDeptId(String deptId) {
        this.deptId = (deptId != null && deptId.trim().isEmpty()) ? null : deptId;
    }
}
