package esea.esea_api.admin.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import esea.esea_api.admin.menu.entities.MenuRole;
import jakarta.transaction.Transactional;

@Repository
public interface MenuRoleRepository extends JpaRepository<MenuRole, Integer> {
    // menuId에 해당하는 MenuRole 목록을 반환
    List<MenuRole> findByMenuId(String menuId);
    
    // roleId로 MenuRole 목록 조회
    List<MenuRole> findByRoleId(int roleId);
    
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM \"MENU_ROLE\" m "
    		+ "WHERE m.\"MENU_ID\" = :menuId", nativeQuery = true)
    void deleteByMenuId(@Param("menuId") String menuId);
    
    // roleIds 배열을 IN 조건으로 조회
    // IN쿼리 예외 처리
    @Query(value = "SELECT * FROM \"MENU_ROLE\" "
    		+ "WHERE \"ROLE_ID\" IN :roleIds "
            + "UNION ALL "
            + "SELECT * FROM \"MENU_ROLE\" WHERE \"ROLE_ID\" = 99", nativeQuery = true)
    List<MenuRole> findMenuRolesByRoleIds(@Param("roleIds") int[] roleIds);
}
