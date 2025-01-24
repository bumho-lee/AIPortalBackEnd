package esea.esea_api.admin.login.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "로그인 DTO")
public class LoginLogSearchDTO {
	
    @Schema(description = "시작일자", example = "20241101", required = false, nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private String startDate;

    @Schema(description = "종료일자", example = "20241130", required = false, nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private String endDate;
    
    @Schema(description = "사용자이름", example = "홍길동", required = false, nullable = true)
    private String userNm;

    @Schema(description = "부서이름", example = "안전팀", required = false, nullable = true)
    private String deptNm;
    
    @Schema(description = "페이지", example = "1", required = false, nullable = true)
    private int page = 1;
    
    @Schema(description = "페이지개수", example = "10", required = false, nullable = true)
    private int size = 10;
}