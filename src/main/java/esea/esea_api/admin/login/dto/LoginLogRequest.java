package esea.esea_api.admin.login.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "로그인 이력 DTO")
public class LoginLogRequest {
	
    @Schema(description = "사용자ID", example = "test1234", required = false, nullable = true)
    private String userId;
    
    @Schema(description = "사용자이름", example = "홍길동", required = false, nullable = true)
    private String userNm;
    
    @Schema(description = "접속IP", example = "0:0:0:0", required = false, nullable = true)
    private String ipAddr;

    @Schema(description = "부서", example = "안전팀", required = false, nullable = true)
    private String deptNm;

    @Schema(description = "부서코드", example = "1234567890", required = false, nullable = true)
    private String deptId;
    
    @Column(name = "LOGIN_YN", length = 1)
    private String loginYn;
    
    @Column(name = "LOGIN_TIME")
    private LocalDateTime loginTime;

}
