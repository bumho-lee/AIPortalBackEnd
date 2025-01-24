package esea.esea_api.admin.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashBoardTransDto {
	private String regDt;
	private Long translatedCharcnt;
	
    public DashBoardTransDto(String regDt, Long translatedCharcnt) {
        this.regDt = regDt;
        this.translatedCharcnt = translatedCharcnt;
    }

}
