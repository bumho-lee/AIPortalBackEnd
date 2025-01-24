package esea.esea_api.admin.role.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import esea.esea_api.admin.role.entities.RoleManager;

@Repository
public interface RoleManagerRepository extends JpaRepository<RoleManager, Integer> {

    @Query(value = "SELECT * FROM \"ROLE_MANAGER\" "
    		+ "WHERE \"ROLE_ID\" = :roleId", nativeQuery = true)
    Page<RoleManager> getAllRoleManagers
    (		
    		@Param("roleId") int roleId,
    		PageRequest pageRequest
    );
    
    // userId를 기준으로 RoleManager 조회
    List<RoleManager> findByUserId(String userId);
    
    
    @Query(value = "SELECT * FROM \"ROLE_MANAGER\" "
    		+ "WHERE CASE \"TYPE\" "
    		+ "    WHEN 'USER' THEN \"USER_ID\" = :userId AND \"DEPT_ID\" = :deptId "
    		+ "    WHEN 'DEPT' THEN \"DEPT_ID\" = :deptId "
    		+ "    END", nativeQuery = true)
    List<RoleManager> findRoleManagerByDeptIdAndUserId(
    		@Param("userId") String userId, 
    		@Param("deptId") String deptId);

    @Query(value = "SELECT * FROM \"ROLE_MANAGER\" "
            + "WHERE \"USER_ID\" = :userId AND \"ROLE_ID\" = :roleId", nativeQuery = true)
    List<RoleManager> findByUserIdAndRoleId(
            @Param("userId") String userId,
            @Param("roleId") int roleId);
}
