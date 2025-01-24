package esea.esea_api.admin.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashBoardUserDto {
	
	private String date;
	private Long count;
	
    public DashBoardUserDto(String date, Long count) {
        this.date = date;
        this.count = count;
    }
}
