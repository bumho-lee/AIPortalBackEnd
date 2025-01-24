package esea.esea_api.admin.role.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.admin.role.dto.DeleteRequestDto;
import esea.esea_api.admin.role.entities.Role;
import esea.esea_api.admin.role.service.RoleService;
import esea.esea_api.translation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "권한 API", description = "권한")
@RestController
@RequestMapping("/role")
public class RoleController {

	private final RoleService roleService;
	
	public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    
	@Operation(summary = "권한 등록", description = "권한 등록")
    @PostMapping("/insert")
	public ResponseEntity<?> createRole(@RequestBody List<Role> roles) {
	    if (roles == null || roles.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Roles list cannot be null or empty.");
	    }

	    List<Role> createdRoles = new ArrayList<>();
	    List<String> errorMessages = new ArrayList<>();

	    for (Role role : roles) {
	        if (role == null) {
	            errorMessages.add("One of the roles in the list is null.");
	            continue;
	        }

	        if (role.getRoleId() == 0) {
	            // If the role has an ID of 0 or null, treat it as a new role (create)
	            try {
	                Role createdRole = roleService.createRole(role);
	                createdRoles.add(createdRole);
	            } catch (Exception e) {
	                errorMessages.add("Failed to create role: " + e.getMessage());
	            }
	        } else {
	            // If the role has a non-zero ID, treat it as an update
	            Role updatedRole = roleService.updateRole(role);
	            if (updatedRole == null) {
	                errorMessages.add("Role with ID " + role.getRoleId() + " not found for update.");
	            } else {
	                createdRoles.add(updatedRole);
	            }
	        }
	    }

	    if (!errorMessages.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
	    }

	    return ResponseEntity.status(HttpStatus.CREATED).body(createdRoles);  // Return the list of created roles
	}

	@Operation(summary = "권한 삭제", description = "권한 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteRoles(@RequestBody DeleteRequestDto deleteRequestDto) {
        roleService.deleteRoles(deleteRequestDto);
        return ResponseEntity.ok("Roles deleted successfully.");
    }
	
    @Operation(summary = "권한 조회", description = "권한 조회")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<Role>> getAllMenus(
    		@RequestParam(value = "page",required = false) int page,
    		@RequestParam(value = "size",required = false) int size) {
    	
        Page<Role> result = roleService.getAllMenus(page, size);
        
        return ResponseEntity.ok(PageResponse.of(result));
    }
    
    @Operation(summary = "메뉴권한 조회", description = "메뉴권한 조회")
    @GetMapping("/menu")
    public ResponseEntity<List<Role>> getAllMenu() {
        return ResponseEntity.ok(roleService.getAllMenu());
    }
}