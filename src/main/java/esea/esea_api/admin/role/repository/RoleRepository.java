package esea.esea_api.admin.role.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import esea.esea_api.admin.role.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "SELECT * FROM \"ROLE\" "
    		+ "", nativeQuery = true)
    Page<Role> getAllMenus(
    		PageRequest pageRequest
    );
}
