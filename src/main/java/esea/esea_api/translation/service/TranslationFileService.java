package esea.esea_api.translation.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import esea.esea_api.translation.entities.TranslationFile;
import esea.esea_api.translation.entities.TranslationManager;
import esea.esea_api.translation.repository.TranslationFileRepository;
import jakarta.transaction.Transactional;

@Service
public class TranslationFileService {

    private final TranslationFileRepository translationFileRepository;

    @Autowired
    public TranslationFileService(TranslationFileRepository translationFileRepository) {
        this.translationFileRepository = translationFileRepository;
    }
    
    public TranslationFile registerTranslationFile(TranslationFile translationFile) {
    	return translationFileRepository.save(translationFile);
    }
    
    public Page<TranslationFile> getFilesByDateRangeAndRegId(
            String startDateTime,
            String endDateTime,
            String userId,
            String fileNm,
            int page,
            int size) {

        // 정렬 설정
        //Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        
        // 페이징 설정
        PageRequest pageRequest = PageRequest.of(page -1, size);

        return translationFileRepository.findByDateRangeAndRegId(
    		startDateTime,
    		endDateTime,
    		userId != null ? userId : "",
			fileNm != null ? fileNm : "",
            pageRequest
        );
    }
    
    public Page<TranslationFile> searchDataList(
            String startDateTime,
            String endDateTime,
            String userNm,
            String deptNm,
            int page,
            int size) {

        // 정렬 설정
        //Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        
        // 페이징 설정
        PageRequest pageRequest = PageRequest.of(page -1, size);

        return translationFileRepository.searchDataList(
    		startDateTime,
    		endDateTime,
    		userNm != null ? userNm : "",
    		deptNm != null ? deptNm : "",
            pageRequest
        );
    }
    
}
