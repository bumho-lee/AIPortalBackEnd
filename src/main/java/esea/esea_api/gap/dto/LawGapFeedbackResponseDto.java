package esea.esea_api.gap.dto;

import esea.esea_api.entities.LawGapFeedback;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.Data;

@Data
@Schema(description = "갭분석 피드백 응답 DTO")
public class LawGapFeedbackResponseDto {
    @Schema(description = "사용자 ID")
    private String userId;

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "부서 ID")
    private String deptId;

    @Schema(description = "부서 이름")
    private String deptName;
    
    @Schema(description = "피드백")
    private String feedback;
    
    @Schema(description = "피드백 내용")
    private String comment;
    
    @Schema(description = "피드백 일자")
    private String date;

    public LawGapFeedbackResponseDto(LawGapFeedback feedback) {
        this.userId = feedback.getUserId();
        this.feedback = feedback.getFeedback();
        this.comment = feedback.getComment();
        this.date = ZonedDateTime.ofInstant(feedback.getCreatedAt().toInstant(), ZoneId.systemDefault())
                                 .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                                 .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
