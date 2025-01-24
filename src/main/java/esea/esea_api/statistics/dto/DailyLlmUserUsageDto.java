package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일일 LLM 사용량 DTO")
public interface DailyLlmUserUsageDto {
    @Schema(description = "사용자 ID")
    String getUserId();

    @Schema(description = "LLM 모델")
    String getLlmModel();

    @Schema(description = "사용 횟수")
    Integer getCount();
    
    @Schema(description = "입력 사용량")
    Integer getInputUsage();
    
    @Schema(description = "출력 사용량")
    Integer getOutputUsage();
    
    @Schema(description = "사용자 이름")
    String getUserName();

    @Schema(description = "부서/부문")
    String getDeptNm();

    @Schema(description = "총 사용량")
    Integer getTotalUsage();

    // 사용자 이름 설정
    void setUserName(String userName);

    // 부서/부문 설정
    void setDeptNm(String deptNm);
}