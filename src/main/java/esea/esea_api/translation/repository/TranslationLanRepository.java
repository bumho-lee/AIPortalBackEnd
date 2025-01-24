package esea.esea_api.translation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import esea.esea_api.translation.entities.CommonCode;
import esea.esea_api.translation.entities.TranslationFile;

@Repository
public interface TranslationLanRepository extends JpaRepository<CommonCode, Integer> {
 
	List<CommonCode> findByCodeType(String codeType);

}