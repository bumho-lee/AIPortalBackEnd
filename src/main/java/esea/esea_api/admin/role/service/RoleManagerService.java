package esea.esea_api.admin.role.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import esea.esea_api.admin.role.dto.DeleteRequestDto;
import esea.esea_api.admin.role.entities.RoleManager;
import esea.esea_api.admin.role.repository.RoleManagerRepository;
import jakarta.transaction.Transactional;

@Service
public class RoleManagerService {
	
	private final RoleManagerRepository roleManagerRepository;
	
    @Autowired
    public RoleManagerService(RoleManagerRepository roleManagerRepository) {
        this.roleManagerRepository = roleManagerRepository;
    }

    // 등록
    @Transactional
    public RoleManager createRoleManager(RoleManager roleManager) {
        return roleManagerRepository.save(roleManager);
    }

    // 수정
    @Transactional
    public RoleManager updateRoleManager(RoleManager roleManager) {
        RoleManager existingRoleManager = roleManagerRepository.findById(roleManager.getRoleManagerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "RoleManager not found with ID: " + roleManager.getRoleManagerId()));
        existingRoleManager.setRoleId(roleManager.getRoleId());
        existingRoleManager.setUserId(roleManager.getUserId());
        existingRoleManager.setType(roleManager.getType());
        return roleManagerRepository.save(existingRoleManager); // 변경된 데이터 저장
    }

    // 삭제
    @Transactional
    public void deleteRoleManagers(DeleteRequestDto deleteRequestDto) {
        if (deleteRequestDto.getIds() == null || deleteRequestDto.getIds().isEmpty()) {
            throw new IllegalArgumentException("삭제할 ID 목록이 비어 있습니다.");
        }
        roleManagerRepository.deleteAllById(deleteRequestDto.getIds());
    }

    // 조회 (리스트)
    public Page<RoleManager> getAllRoleManagers(
    		int roleId,
            int page,
            int size
        ){
        // 페이징 설정
        PageRequest pageRequest = PageRequest.of(page -1, size);
    	
        return roleManagerRepository.getAllRoleManagers(
        	roleId,
            pageRequest
        );
    }

    // userId로 RoleManager 목록을 조회하고 첫 번째 데이터 반환
    public List<RoleManager> getAllRoleManager(String userId) {
        return roleManagerRepository.findByUserId(userId);  // 해당 데이터가 없으면 null 반환
    }
    
    // JPQL을 사용하여 조회
    public List<RoleManager> findRoleManagerByDeptIdAndUserId(String userId, String deptId) {
        return roleManagerRepository.findRoleManagerByDeptIdAndUserId(userId, deptId);
    }
}
