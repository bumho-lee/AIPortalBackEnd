package esea.esea_api.translation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "번역 DTO")
public class TranslationFileSearchDataDTO {
	
    @Schema(description = "시작일자", example = "20241101", required = false, nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private String startDate;

    @Schema(description = "종료일자", example = "20241130", required = false, nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private String endDate;
    
    @Schema(description = "등록자ID", example = "테스", required = false, nullable = true)
    private String userNm;
    
    @Schema(description = "부서명", example = "DT", required = false, nullable = true)
    private String deptNm;
    
    @Schema(description = "페이지", example = "1", required = false, nullable = true)
    private int page = 1;
    
    @Schema(description = "페이지개수", example = "10", required = false, nullable = true)
    private int size = 10;
}