package esea.esea_api.gap.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "갭분석 목록 조회 DTO")
public class LawGapRequestDto {
    @Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "시작일", example = "20240101", nullable = true, required = false)
    private String startDate;

    @Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "종료일", example = "20240101", nullable = true, required = false)
    private String endDate;

    @Schema(description = "키워드", example = "키워드", nullable = true, required = false)
    private String keyword;

    @Schema(description = "피드백 존재 여부", example = "Y | N", nullable = true, required = false)
    private String feedbackYn;

    @Schema(description = "관리자 검색", example = "관리자 키워드", nullable = true, required = false)
    private String managerName;

    @Schema(description = "검토자 검색", example = "검토자 키워드", nullable = true, required = false)
    private String reviewerName;

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

    public void setFeedbackYn(String feedbackYn) {
        this.feedbackYn = (feedbackYn != null && !feedbackYn.trim().isEmpty()) ? feedbackYn : null;
    }

    public void setmanagerName(String managerName) {
        this.managerName = (managerName != null && !managerName.trim().isEmpty()) ? managerName : null;
    }

    public void setreviewerName(String reviewerName) {
        this.reviewerName = (reviewerName != null && !reviewerName.trim().isEmpty()) ? reviewerName : null;
    }
}
