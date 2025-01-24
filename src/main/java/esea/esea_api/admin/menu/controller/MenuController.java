package esea.esea_api.admin.menu.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.admin.filtering.dto.DeleteRequestDto;
import esea.esea_api.admin.menu.dto.MenuRoleResponse;
import esea.esea_api.admin.menu.dto.MenuUpdateRequestDto;
import esea.esea_api.admin.menu.dto.MenuViewResponse;
import esea.esea_api.admin.menu.entities.Menu;
import esea.esea_api.admin.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "메뉴 API", description = "메뉴 API")
@RestController
@RequestMapping("/menu")
public class MenuController {
	
	private final MenuService menuService;
	
	public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    
	@Operation(summary = "메뉴 등록", description = "메뉴 등록")
    @PostMapping("/insert")
    public void createMenus(@RequestBody Menu menu) {
        menuService.createMenus(menu);
    }
    
	@Operation(summary = "메뉴 수정", description = "메뉴 수정")
	@PostMapping("/update")
    public void updateMenus(@RequestBody MenuUpdateRequestDto requestDto) {
        menuService.updateMenus(requestDto);
    }
    
	@Operation(summary = "메뉴 삭제", description = "메뉴 삭제")
	@PostMapping("/delete")
    public ResponseEntity<String> deleteMenus(@RequestBody DeleteRequestDto deleteRequestDto) {
        try {
            menuService.deleteMenus(deleteRequestDto);
            
            return ResponseEntity.ok("Menus deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting menus.");
        }
    }
    
	@Operation(summary = "메뉴 조회", description = "메뉴 조회")
    @GetMapping("/search")
    public ResponseEntity<List<MenuRoleResponse>> getMenuRoles() {
        List<MenuRoleResponse> menuRoles = menuService.getMenuRoles();
        return ResponseEntity.ok(menuRoles);
    }
	
	@Operation(summary = "메뉴 View 조회", description = "메뉴 View 조회")
    @GetMapping("/searchView")
    public ResponseEntity<List<MenuViewResponse>> getMenuViews() {
        List<MenuViewResponse> menuRoles = menuService.getMenuViews();
        return ResponseEntity.ok(menuRoles);
    }
	
}
