package esea.esea_api.admin.menu.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MenuRoleResponse {

	private int id;
    private String menu_id;
    private String menu_name;
    private String path;
    private String icon;
    private int level;
    private String parentId;
    private String[] roleIds;
    private String useYn;
    private String viewYn;
    private int menuOrder;

    public MenuRoleResponse(int id, String menu_id, String menu_name,String path, String icon, int level, String parentId, String[] roleIds, String useYn, String viewYn, int menuOrder) {
    	this.id = id;
    	this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.path = path;
        this.icon = icon;
        this.level = level;
        this.parentId = parentId;
        this.roleIds = roleIds;
        this.useYn = useYn;
        this.viewYn = viewYn;
        this.menuOrder = menuOrder;
    }

    // Getters and Setters
}
