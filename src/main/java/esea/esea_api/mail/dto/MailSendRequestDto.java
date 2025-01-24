package esea.esea_api.mail.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Schema(description = "메일 발송 요청")
public class MailSendRequestDto {
    @Schema(description = "수신자")
    private List<String> to;

    @Schema(description = "발신자")
    private String from;
    
    @Schema(description = "제목")
    private String subject;
    
    @Schema(description = "이메일 파라미터")
    private Map<String, Object> params;
}
