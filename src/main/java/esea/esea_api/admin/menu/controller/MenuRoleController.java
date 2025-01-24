package esea.esea_api.admin.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.admin.menu.dto.MenuRoleRequest;
import esea.esea_api.admin.menu.entities.MenuRole;
import esea.esea_api.admin.menu.service.MenuRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "권한 API", description = "권한")
@RestController
@RequestMapping("/role")
public class MenuRoleController {
	
	private final MenuRoleService menuRoleService;
	
    @Autowired
    public MenuRoleController(MenuRoleService menuRoleService) {
    	this.menuRoleService = menuRoleService;
    }
	
    @Operation(summary = "메뉴 ROLE 조회", description = "메뉴 ROLE 조회")
    @GetMapping("/roleSearch")
    public ResponseEntity<List<MenuRole>> getAllMenuRoles(
            @RequestParam(value = "roleId") int roleId) {

        List<MenuRole> result = menuRoleService.getMenuRolesByRoleId(roleId);
        
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.noContent().build(); // 결과가 없을 때는 204 No Content
        }
    }
    
    @Operation(summary = "메뉴 ROLE 조회", description = "메뉴 ROLE 조회")
    @GetMapping("/roleSearchs")
    public ResponseEntity<List<MenuRole>> getAllMenuRole(
            @RequestParam(value = "roleId") int[] roleIds) {

        List<MenuRole> result = menuRoleService.getMenuRolesByRoleIds(roleIds);
        
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.noContent().build(); // 결과가 없을 때는 204 No Content
        }
    }
    
    @Operation(summary = "메뉴 ROLE 등록", description = "메뉴 ROLE 등록")
    @PostMapping("/roleInsert")
    public ResponseEntity<?> createMenuRoles(@RequestBody List<MenuRoleRequest> menuRoles) {
        List<MenuRole> savedMenuRoles = menuRoleService.createMenuRoles(menuRoles);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }
    
	/*
	 * @Operation(summary = "메뉴 ROLE 수정", description = "메뉴 ROLE 수정")
	 * 
	 * @PostMapping("/roleUpdate") public ResponseEntity<List<MenuRole>>
	 * updateMenuRoles(@RequestBody List<MenuRole> menuRoles) { List<MenuRole>
	 * updatedMenuRoles = menuRoleService.updateMenuRoles(menuRoles);
	 * 
	 * return ResponseEntity.ok(updatedMenuRoles); }
	 * 
	 * @Operation(summary = "메뉴 ROLE 삭제", description = "메뉴 ROLE 삭제")
	 * 
	 * @PostMapping("/roleDelete") public ResponseEntity<String>
	 * deleteMenuRoles(@RequestBody List<Integer> menuRoleIds) { try {
	 * menuRoleService.deleteMenuRoles(menuRoleIds);
	 * 
	 * return ResponseEntity.ok("Menu roles deleted successfully."); } catch
	 * (Exception e) { return
	 * ResponseEntity.status(500).body("Error deleting menu roles."); } }
	 */

}
