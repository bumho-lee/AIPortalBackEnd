package esea.esea_api.admin.llm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import esea.esea_api.admin.llm.entities.LlmEntService;
import jakarta.transaction.Transactional;

public interface LlmRepository extends JpaRepository<LlmEntService, Integer> {
    @Query(value = "SELECT * FROM \"LLM_SERVICE\" "
    		+ "WHERE \"NAME\" ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
    Page<LlmEntService> findByNameLike(
    		@Param("name") String name,
    		PageRequest pageRequest
    );
    
    @Modifying
    @Transactional
    @Query(value ="DELETE FROM \"LLM_SERVICE\" "
    		+ "WHERE \"LLM_SERVICE_ID\" = :llmServiceId", nativeQuery = true)
    void deleteLlmById(@Param("llmServiceId") int llmServiceId);
    
    // status가 ACTIVE인 엔티티 조회
    List<LlmEntService> findByStatus(String status);

    LlmEntService findByServiceKey(String serviceKey);
}
