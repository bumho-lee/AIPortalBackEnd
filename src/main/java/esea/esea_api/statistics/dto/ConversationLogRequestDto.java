package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "대화 로그 요청 DTO")
public class ConversationLogRequestDto {
@Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "시작일", example = "20240101", nullable = true, required = false)
    private String startDate;

    @Pattern(regexp = "^$|^\\d{8}$", message = "날짜는 8자리 숫자(YYYYMMDD) 형식이어야 합니다")
    @Schema(description = "종료일", example = "20240101", nullable = true, required = false)
    private String endDate;

    @Schema(description = "유저 아이디", example = "1", required = true, nullable = false)
    private String userId;

    @Schema(description = "좋아요/싫어요", example = "LIKE | DISLIKE", required = true, nullable = false)
    private String likeDislike;

    @Schema(description = "model 서비스 키", example = "bedrock", nullable = true, required = false)
    private String model;

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

    public void setLikeDislike(String likeDislike) {
        this.likeDislike = (likeDislike != null && likeDislike.trim().isEmpty()) ? null : likeDislike;
    }

    public void setUserId(String userId) {
        this.userId = (userId != null && userId.trim().isEmpty()) ? null : userId;
    }
}
