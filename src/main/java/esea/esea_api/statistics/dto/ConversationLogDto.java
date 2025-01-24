package esea.esea_api.statistics.dto;

import esea.esea_api.entities.ConversationLog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;
@Schema(description = "대화 로그 DTO")
@Data
public class ConversationLogDto {
    @Schema(description = "대화 로그 ID")
    private Integer conversationLogId;

    @Schema(description = "사용자 ID")
    private String query;
    
    @Schema(description = "답변")
    private String answer;

    @Schema(description = "사용자 ID")
    private String userId;

    @Schema(description = "사용자 이름")
    private String userName;

    @Schema(description = "대화 평가")
    private String likeDislike;

    @Schema(description = "싫어요 이유")
    private String reason;

    @Schema(description = "등록일")
    private String regDt;

    @Schema(description = "선택한 지식 체계")
    private List<Integer> knowledgeIds;
    
    @Schema(description = "모델")
    private String model;

    private List<String> knowledgeNames;

    public ConversationLogDto(ConversationLog conversationLog) {
        this.conversationLogId = conversationLog.getConversationLogId();
        this.query = conversationLog.getQuery();
        this.answer = conversationLog.getAnswer();
        this.userId = conversationLog.getUserId();
        this.likeDislike = conversationLog.getType();
        this.reason = conversationLog.getReason();
        this.model = conversationLog.getLlmModel();
        
        this.regDt = conversationLog.getRegDt()
            .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
            .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }
}
