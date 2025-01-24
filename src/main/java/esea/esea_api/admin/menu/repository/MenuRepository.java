package esea.esea_api.admin.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import esea.esea_api.admin.menu.entities.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    
    @Query(value = "SELECT m.\"ID\", m.\"MENU_ID\", m.\"MENU_NAME\", m.\"PATH\", m.\"ICON\", m.\"LEVEL\", m.\"PARENT_ID\", " +
            "STRING_AGG(DISTINCT CASE WHEN mr.\"ROLE_ID\" IS NOT NULL THEN mr.\"ROLE_ID\"::text ELSE NULL END, ',') AS ROLE_IDS, " +
            "m.\"USE_YN\", m.\"VIEW_YN\", m.\"MENU_ORDER\" " +
            "FROM \"MENUS\" m " +
            "LEFT JOIN \"MENU_ROLE\" mr ON m.\"MENU_ID\" = mr.\"MENU_ID\" " +
            "GROUP BY  m.\"ID\", m.\"MENU_ID\", m.\"MENU_NAME\", m.\"PATH\", m.\"ICON\", m.\"LEVEL\", m.\"PARENT_ID\", m.\"USE_YN\", m.\"VIEW_YN\", m.\"MENU_ORDER\" " +
            "ORDER BY m.\"MENU_ID\"", nativeQuery = true)
    List<Object[]> findMenuRolesRaw();
    
    @Query(value = "SELECT m.\"ID\", m.\"MENU_ID\", m.\"MENU_NAME\", m.\"PATH\", m.\"ICON\", m.\"LEVEL\", m.\"PARENT_ID\", m.\"VIEW_YN\" " +
            "FROM \"MENUS\" m " +
            "LEFT JOIN \"MENU_ROLE\" mr ON m.\"MENU_ID\" = mr.\"MENU_ID\" " +
            "LEFT JOIN \"ROLE\" r ON mr.\"ROLE_ID\" = r.\"ROLE_ID\" " +
            "WHERE m.\"USE_YN\" = 'Y' AND r.\"USE_YN\" = 'Y' " +
            "GROUP BY  m.\"ID\", m.\"MENU_ID\", m.\"MENU_NAME\", m.\"PATH\", m.\"ICON\", m.\"LEVEL\", m.\"PARENT_ID\", m.\"USE_YN\" " +
            "ORDER BY m.\"MENU_ID\"", nativeQuery = true)
    List<Object[]> findMenuViewRaw();
}
