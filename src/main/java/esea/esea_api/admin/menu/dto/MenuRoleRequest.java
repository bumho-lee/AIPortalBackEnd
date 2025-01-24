package esea.esea_api.admin.menu.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRoleRequest {
    private String menu_id;
    private List<Integer> roleIds;
}
