package esea.esea_api.admin.menu.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRequestDto {
    private List<Integer> ids; // 삭제할 ID 목록
}
