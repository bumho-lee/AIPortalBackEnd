package esea.esea_api.translation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import esea.esea_api.translation.entities.TranslationManager;

@Repository
public interface TranslationManagerRepository extends JpaRepository<TranslationManager, Integer> {
  
    /**
     * DEPARTMENT_ID를 기준으로 TranslationManager 조회
     */
    @Query(value = "SELECT * FROM \"TRANSLATION_MANAGER\" "
    		+ "WHERE \"DEPARTMENT_ID\" = :departmentId", nativeQuery = true)
    TranslationManager findByDepartmentId(@Param("departmentId") String departmentId);
    
    // 전체 조회
    @Query(value = "SELECT " +
            "\"TRANSLATION_ID\", " +
            "\"API_KEY\", " +
            "\"DEPARTMENT\", " +
            "\"TRANSLATION_LIMIT\", " +
            "\"TRANSLATION_USAGE\", " +
            "\"TRANSLATION_USAGE_RATE\", " +
            "\"REG_ID\", " +
            "\"REG_DT\", " +
            "\"DEPARTMENT_ID\", " +
            "\"UPDATE_DT\", " +
            "CEIL(\"TRANSLATION_USAGE\" / 1000000.0) * 25 AS COST " +
            "FROM \"TRANSLATION_MANAGER\"", nativeQuery = true)
    List<TranslationManager> findAll();

}