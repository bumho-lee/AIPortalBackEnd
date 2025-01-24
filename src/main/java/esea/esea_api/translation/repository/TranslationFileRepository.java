package esea.esea_api.translation.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import esea.esea_api.translation.entities.TranslationFile;
import esea.esea_api.translation.entities.TranslationManager;
import jakarta.transaction.Transactional;

@Repository
public interface TranslationFileRepository extends JpaRepository<TranslationFile, Integer> {
	
    @Query(value ="SELECT * FROM \"TRANSLATION_FILES\" " +
    		 "WHERE TO_CHAR(\"REG_DT\", 'YYYYMMDD') BETWEEN :startDate AND :endDate " +
    		 "AND \"REG_ID\" = :regId " +
    		 "AND \"ORIGINAL_FILENAME\" ILIKE '%' || :fileNm || '%' " +
    		 "ORDER BY \"FILE_ID\" DESC", nativeQuery = true)
     Page<TranslationFile> findByDateRangeAndRegId(
         @Param("startDate") String startDateTime,
         @Param("endDate") String endDateTime,
         @Param("regId") String regId,
         @Param("fileNm") String fileNm,
         PageRequest pageRequest
     );
    
    @Query(value ="SELECT * FROM \"TRANSLATION_FILES\" " +
   		 "WHERE TO_CHAR(\"REG_DT\", 'YYYYMMDD') BETWEEN :startDate AND :endDate " +
   		 "AND \"DEPT_NM\" ILIKE '%' || :deptNm || '%' " +
   		 "AND \"REG_NM\" ILIKE '%' || :userNm || '%' " +
   		 "ORDER BY \"FILE_ID\" DESC", nativeQuery = true)
    Page<TranslationFile> searchDataList(
        @Param("startDate") String startDateTime,
        @Param("endDate") String endDateTime,
        @Param("userNm") String userNm,
        @Param("deptNm") String deptNm,
        PageRequest pageRequest
    );

}