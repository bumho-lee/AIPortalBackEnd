package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "유저별 사용량 엑셀 다운로드 DTO")
public class UserLlmUsageExcelRequestDto {
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
