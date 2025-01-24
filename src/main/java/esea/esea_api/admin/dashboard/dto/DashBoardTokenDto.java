package esea.esea_api.admin.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashBoardTokenDto {

	private String tokenDate;
	private String llmModel;
	private Long tokenInputCount;
	private Long tokenOutPutCount;
	private Long tokenRecordCount;
	private Long tokenSum;
	
    public DashBoardTokenDto(String tokenDate, String llmModel, Long tokenInputCount, Long tokenOutPutCount, Long tokenRecordCount, Long tokenSum) {
        this.tokenDate = tokenDate;
        this.llmModel = llmModel;
        this.tokenInputCount = tokenInputCount;
        this.tokenOutPutCount = tokenOutPutCount;
        this.tokenRecordCount = tokenRecordCount;
        this.tokenSum = tokenSum;
    }
}
