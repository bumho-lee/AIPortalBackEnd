package esea.esea_api.admin.role.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.admin.role.dto.DeleteRequestDto;
import esea.esea_api.admin.role.entities.RoleManager;
import esea.esea_api.admin.role.service.RoleManagerService;
import esea.esea_api.translation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "권한 manager API", description = "권한 manager API")
@RestController
@RequestMapping("/roleManager")
public class RoleManagerController {

	private final RoleManagerService roleManagerService;
	
	public RoleManagerController(RoleManagerService roleManagerService) {
        this.roleManagerService = roleManagerService;
    }

    // 등록 API
	@Operation(summary = "권한 manager 등록", description = "권한 manager 등록")
    @PostMapping("/insert")
    public ResponseEntity<RoleManager> createRoleManager(@RequestBody RoleManager roleManager) {
        RoleManager createdRoleManager = roleManagerService.createRoleManager(roleManager);
        return ResponseEntity.ok(createdRoleManager);
    }

    // 수정 API
    @Operation(summary = "권한 manager 수정", description = "권한 manager 수정")
    @PostMapping("/update")
    public ResponseEntity<RoleManager> updateRoleManager(@RequestBody RoleManager roleManager) {
        if (roleManager.getRoleManagerId() == null) {
            return ResponseEntity.badRequest().body(null); // ID가 없으면 Bad Request 반환
        }
        RoleManager updatedRoleManager = roleManagerService.updateRoleManager(roleManager);
        return ResponseEntity.ok(updatedRoleManager);
    }

    // 삭제 API
    @Operation(summary = "권한 manager 삭제", description = "권한 manager 삭제")
    @PostMapping("/delete")
    public ResponseEntity<?> deleteRoleManagers(@RequestBody DeleteRequestDto deleteRequestDto) {
        roleManagerService.deleteRoleManagers(deleteRequestDto);
        return ResponseEntity.ok("RoleManagers deleted successfully.");
    }

    @Operation(summary = "권한 manager 조회", description = "권한 manager 조회")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<RoleManager>> getAllRoleManagers(
    		@RequestParam(value = "roleId",required = false) int roleId,
    		@RequestParam(value = "page",required = false) int page,
    		@RequestParam(value = "size",required = false) int size) {
    	
        Page<RoleManager> result = roleManagerService.getAllRoleManagers(roleId, page, size);
        
        return ResponseEntity.ok(PageResponse.of(result));
    }
    
    @Operation(summary = "권한 manager 조회", description = "권한 manager 조회")
    @GetMapping("/searchManager")
    public ResponseEntity<?> getAllRoleManager(
            @RequestParam(value = "userId", required = false) String userId) {

        List<RoleManager> result = roleManagerService.getAllRoleManager(userId);

        if (result != null) {
            return ResponseEntity.ok(result);
        } else {
        	Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "등록된 사용자가 아닙니다.");
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    @Operation(summary = "권한 manager 조회", description = "권한 manager 조회")
    @GetMapping("/searchManagers")
    public ResponseEntity<List<RoleManager>> test(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "deptId") String deptId) {

        List<RoleManager> result = roleManagerService.findRoleManagerByDeptIdAndUserId(userId, deptId);
        return ResponseEntity.ok(result);
    }
}
