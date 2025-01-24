package esea.esea_api.admin.menu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import esea.esea_api.admin.menu.dto.MenuRoleRequest;
import esea.esea_api.admin.menu.entities.MenuRole;
import esea.esea_api.admin.menu.repository.MenuRoleRepository;
import jakarta.transaction.Transactional;

@Service
public class MenuRoleService {

    private final MenuRoleRepository menuRoleRepository;

    @Autowired
    public MenuRoleService(MenuRoleRepository menuRoleRepository) {
        this.menuRoleRepository = menuRoleRepository;
    }

    // roleId로 MenuRole 목록 조회
    public List<MenuRole> getMenuRolesByRoleId(int roleId) {
        return menuRoleRepository.findByRoleId(roleId);
    }
    
    @Transactional
    public List<MenuRole> createMenuRoles(List<MenuRoleRequest> menuRoleRequests) {
        List<MenuRole> allSavedMenuRoles = new ArrayList<>();
        menuRoleRepository.deleteByMenuId(menuRoleRequests.get(0).getMenu_id());
        for (MenuRoleRequest menuRoleRequest : menuRoleRequests) {
            String menuId = menuRoleRequest.getMenu_id();
            List<Integer> roleIds = menuRoleRequest.getRoleIds();

            List<MenuRole> existingMenuRoles = menuRoleRepository.findByMenuId(menuId);

            for (Integer roleId : roleIds) {
                Optional<MenuRole> existingRoleOpt = existingMenuRoles.stream()
                    .filter(role -> role.getRoleId() == roleId)
                    .findFirst();

                if (existingRoleOpt.isPresent()) {
                    MenuRole existingRole = existingRoleOpt.get();
                    allSavedMenuRoles.add(existingRole);
                } else {
                    MenuRole newMenuRole = new MenuRole();
                    newMenuRole.setMenuId(menuId);
                    newMenuRole.setRoleId(roleId);
                    menuRoleRepository.save(newMenuRole);
                    allSavedMenuRoles.add(newMenuRole);
                }
            }
        }

        // Return all saved or updated MenuRole objects
        return allSavedMenuRoles;
    }
    
    public List<MenuRole> updateMenuRoles(List<MenuRole> menuRoles) {
        for (MenuRole newMenuRole : menuRoles) {
            Optional<MenuRole> existingMenuRoleOpt = menuRoleRepository.findById(newMenuRole.getMenuRoleId());
  
            if (existingMenuRoleOpt.isPresent()) {
                
				MenuRole existingMenuRole = existingMenuRoleOpt.get();
               
                if (newMenuRole.getMenuId() != null) existingMenuRole.setMenuId(newMenuRole.getMenuId());
                if (newMenuRole.getRoleId() != 0) existingMenuRole.setRoleId(newMenuRole.getRoleId());

                menuRoleRepository.save(existingMenuRole);  
            }
        }
        return menuRoleRepository.findAllById(menuRoles.stream().map(MenuRole::getMenuRoleId).toList());
    }
    
    @Transactional
    public void deleteMenuRoles(List<Integer> menuRoleIds) {
    	for (Integer menuRoleId : menuRoleIds) {
            menuRoleRepository.deleteById(menuRoleId); // Delete each menu role by its ID
        }
    }
    
    // roleIds 배열로 MenuRole 조회
    public List<MenuRole> getMenuRolesByRoleIds(int[] roleIds) {
        return menuRoleRepository.findMenuRolesByRoleIds(roleIds);
    }
}
