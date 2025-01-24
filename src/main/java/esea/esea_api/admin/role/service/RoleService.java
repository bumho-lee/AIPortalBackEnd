package esea.esea_api.admin.role.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import esea.esea_api.admin.role.dto.DeleteRequestDto;
import esea.esea_api.admin.role.entities.Role;
import esea.esea_api.admin.role.repository.RoleRepository;
import jakarta.transaction.Transactional;

@Service
public class RoleService {
	
	private final RoleRepository roleRepository;
	
    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    public List<Role> getAllMenu() {
        return roleRepository.findAll();
    }

    public Page<Role> getAllMenus(
            int page,
            int size
        ){
        // 페이징 설정
        PageRequest pageRequest = PageRequest.of(page -1, size);
        return roleRepository.getAllMenus(
            pageRequest
        );
    }
    
    // 등록
    @Transactional
    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    // 수정
    @Transactional
    public Role updateRole(Role role) {
        Role existingRole = roleRepository.findById(role.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + role.getRoleId()));
        // 필요한 필드만 업데이트
        existingRole.setName(role.getName());
        existingRole.setUseYn(role.getUseYn());
        existingRole.setRegId(role.getRegId());
        return roleRepository.save(existingRole); // 변경된 데이터 저장
    }

    // 삭제 (수정된 부분)
    @Transactional
    public void deleteRoles(DeleteRequestDto deleteRequestDto) {
        if (deleteRequestDto.getIds() == null || deleteRequestDto.getIds().isEmpty()) {
            throw new IllegalArgumentException("삭제할 ID 목록이 비어 있습니다.");
        }
        roleRepository.deleteAllById(deleteRequestDto.getIds());
    }

}
