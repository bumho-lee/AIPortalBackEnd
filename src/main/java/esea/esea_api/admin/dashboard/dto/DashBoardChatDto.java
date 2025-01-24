package esea.esea_api.admin.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashBoardChatDto {

	private String date;
	private Long count;
	
    public DashBoardChatDto(String date, Long count) {
        this.date = date;
        this.count = count;
    }
}
