package esea.esea_api.gap.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@NoArgsConstructor
public class LawGapUserInfoDto {
    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    @Schema(description = "사용자 아이디", example = "1234567890")
    private String userId;
    
    @Schema(description = "부서 이름", example = "경영지원팀")
    private String deptName;
    
    @Schema(description = "부서 아이디", example = "1234567890")
    private String deptId;
}
