package esea.esea_api.admin.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import esea.esea_api.admin.filtering.dto.DeleteRequestDto;
import esea.esea_api.admin.menu.dto.MenuRoleResponse;
import esea.esea_api.admin.menu.dto.MenuUpdateRequestDto;
import esea.esea_api.admin.menu.dto.MenuViewResponse;
import esea.esea_api.admin.menu.entities.Menu;
import esea.esea_api.admin.menu.repository.MenuRepository;
import jakarta.transaction.Transactional;

@Service
public class MenuService {
	
	private final MenuRepository menuRepository;
	
    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    // Fetch all Menu entries
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
    
    @Transactional
    public void createMenus(Menu menu) {
        menuRepository.save(menu);
    }
    
    @Transactional
    public void updateMenus(MenuUpdateRequestDto requestDto) {
        Optional<Menu> existingMenuOpt = menuRepository.findById(requestDto.getId());
        
        if (existingMenuOpt.isPresent()) {
            Menu existingMenu = existingMenuOpt.get();
            existingMenu.setParentId(requestDto.getParentId());
            existingMenu.setMenuOrder(requestDto.getMenuOrder());
            existingMenu.setMenu_id(requestDto.getMenu_id());
            existingMenu.setMenu_name(requestDto.getMenu_name());
            existingMenu.setViewYn(requestDto.getViewYn());
            existingMenu.setUseYn(requestDto.getUseYn());
            existingMenu.setPath(requestDto.getPath());
            
            menuRepository.save(existingMenu);
        }
    }

    @Transactional
    public void deleteMenus(DeleteRequestDto deleteRequestDto) {
        if (deleteRequestDto.getIds() == null || deleteRequestDto.getIds().isEmpty()) {
            throw new IllegalArgumentException("삭제할 ID 목록이 비어 있습니다.");
        }
        menuRepository.deleteAllById(deleteRequestDto.getIds());
    }
    
    public List<MenuRoleResponse> getMenuRoles() {
        List<Object[]> rawData = menuRepository.findMenuRolesRaw();
        List<MenuRoleResponse> responses = new ArrayList<>();
        
        for (Object[] data : rawData) {
        	int id = (int) data[0];
            String menu_id = (String) data[1];
            String menu_name = (String) data[2];
            String path = (String) data[3];
            String icon = (String) data[4];
            int level = Optional.ofNullable(data[5]).map(d -> d.toString().isEmpty() ? 0 : Integer.parseInt(d.toString())).orElse(0);
            String parentId = (String) data[6];
            String roleId = (String) data[7];
            String useYn = (String) data[8];
            String viewYn = (String) data[9];
            int menuOrder = (int) data[10];

            String[] roleIds;
            
            if(roleId != null) {
            	roleIds = roleId.split(",");
            }else {
            	roleIds = new String[] { "0" };
            }
            
            useYn = (useYn != null && "Y".equalsIgnoreCase(useYn)) ? "Y" : "N";
            
            MenuRoleResponse response = new MenuRoleResponse(id, menu_id, menu_name, path, icon, level, parentId, roleIds, useYn, viewYn, menuOrder);
            responses.add(response);
        }
        
        return responses;
    }
    
    public List<MenuViewResponse> getMenuViews() {
        List<Object[]> rawData = menuRepository.findMenuViewRaw();
        List<MenuViewResponse> responses = new ArrayList<>();
        
        for (Object[] data : rawData) {
        	int id = (int) data[0];
            String menu_id = (String) data[1];
            String menu_name = (String) data[2];
            String path = (String) data[3];
            String icon = (String) data[4];
            int level = Optional.ofNullable(data[5]).map(d -> d.toString().isEmpty() ? 0 : Integer.parseInt(d.toString())).orElse(0);
            String parentId = (String) data[6];
            String viewYn  = (String) data[7];

            MenuViewResponse response = new MenuViewResponse(id, menu_id, menu_name, path, icon, level, parentId, viewYn);
            responses.add(response);
        }
        
        return responses;
    }
}
