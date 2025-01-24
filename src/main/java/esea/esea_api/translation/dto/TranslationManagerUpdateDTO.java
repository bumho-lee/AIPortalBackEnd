package esea.esea_api.translation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "번역 사용량 DTO")
public class TranslationManagerUpdateDTO {
    private int translationId; // 수정할 TranslationManager의 ID

    private String apiKey; // API Key

    private String department; // 부서
    
    private String department_id; // 부서

    private int translationLimit; // 번역 제한

    private int translationUsage; // 사용량

    private int translationUsageRate; // 사용률

    private String regId; // 등록자 ID
    
    private String updateDt;
}