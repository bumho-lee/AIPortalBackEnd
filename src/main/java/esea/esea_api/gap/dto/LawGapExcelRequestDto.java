package esea.esea_api.gap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "갭분석 엑셀 조회 DTO")
public class LawGapExcelRequestDto {
    @Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "시작일", example = "20240101", nullable = true, required = false)
    private String startDate;

    @Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "종료일", example = "20240101", nullable = true, required = false)
    private String endDate;

    @Schema(description = "키워드", example = "키워드", nullable = true, required = false)
    private String keyword;

    @Schema(description = "피드백 존재 여부", example = "피드백 유형", nullable = true, required = false)
    private String feedbackYn;

    @Schema(description = "관리자 이름", example = "관리자 이름", nullable = true, required = false)
    private String managerName;

    @Schema(description = "검토자 이름", example = "검토자 이름", nullable = true, required = false)
    private String reviewerName;

    public void setStartDate(String startDate) {
        this.startDate = (startDate != null && startDate.trim().isEmpty()) ? null : startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = (endDate != null && endDate.trim().isEmpty()) ? null : endDate;
    }

    public void setKeyword(String keyword) {
        this.keyword = (keyword != null && keyword.trim().isEmpty()) ? null : keyword;
    }

    public void setFeedbackYn(String feedbackYn) {
        this.feedbackYn = (feedbackYn != null && feedbackYn.trim().isEmpty()) ? null : feedbackYn;
    }

    public void setManagerName(String managerName) {
        this.managerName = (managerName != null && managerName.trim().isEmpty()) ? null : managerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = (reviewerName != null && reviewerName.trim().isEmpty()) ? null : reviewerName;
    }
}
