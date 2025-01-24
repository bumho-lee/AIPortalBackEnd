package esea.esea_api.translation.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import esea.esea_api.translation.dto.DeleteTranslationManager;
import esea.esea_api.translation.entities.TranslationManager;
import esea.esea_api.translation.repository.TranslationManagerRepository;
import jakarta.transaction.Transactional;

@Service
public class TranslationManagerService {

    private final TranslationManagerRepository translationManagerRepository;

    @Autowired
    public TranslationManagerService(TranslationManagerRepository translationManagerRepository) {
        this.translationManagerRepository = translationManagerRepository;
    }
    

    /**
     * DEPARTMENT_ID로 TranslationManager 데이터 조회
     *
     * @param departmentId 부문 ID
     * @return TranslationManager (Optional)
     */
    public TranslationManager getByDepartmentId(String departmentId) {
        return translationManagerRepository.findByDepartmentId(departmentId);
    }
    
    // TranslationManager 수정
    public void updateTranslationManager(int translationId, String apiKey, String department, String department_id, int translationLimit,
                                          int translationUsage, BigDecimal translationUsageRate) {

        TranslationManager translationManager = translationManagerRepository.findById(translationId)
                .orElseThrow(() -> new RuntimeException("TranslationManager not found for id: " + translationId));

        translationManager.setApiKey(apiKey);
        translationManager.setDepartment(department);
        translationManager.setDepartment_id(department_id);
        translationManager.setTranslationLimit(translationLimit);
        translationManager.setTranslationUsage(translationUsage);
        translationManager.setTranslationUsageRate(translationUsageRate);
        translationManager.setUpdateDt(LocalDateTime.now());

        translationManagerRepository.save(translationManager);
    }
    
    // 전체 조회
    public List<TranslationManager> getAllTranslationManagers() {
        return translationManagerRepository.findAll();
    }
    
    // 등록 (여러 항목을 한 번에)
    public List<TranslationManager> saveTranslationManagers(List<TranslationManager> translationManagers) {
        return translationManagerRepository.saveAll(translationManagers);
    }
    
    @Transactional
    public void deleteTranslationManagers(DeleteTranslationManager deleteTranslationManager) {
        if (deleteTranslationManager.getIds() == null || deleteTranslationManager.getIds().isEmpty()) {
            throw new IllegalArgumentException("삭제할 ID 목록이 비어 있습니다.");
        }
        translationManagerRepository.deleteAllById(deleteTranslationManager.getIds());
    }
    
    // 수정 (변경된 필드만 업데이트)
    @Transactional
    public List<TranslationManager> updateTranslationManagers(List<TranslationManager> translationManagers) {
        for (TranslationManager newTranslationManager : translationManagers) {
        	
        	if(newTranslationManager.getTranslationId() != 0) {
	            // 기존 TranslationManager 조회
	            Optional<TranslationManager> existingTranslationManagerOpt = translationManagerRepository.findById(newTranslationManager.getTranslationId());
	
	            if (existingTranslationManagerOpt.isPresent()) {
	                TranslationManager existingTranslationManager = existingTranslationManagerOpt.get();
	
	                // 변경할 값만 업데이트
	                if (newTranslationManager.getApiKey() != null) existingTranslationManager.setApiKey(newTranslationManager.getApiKey());
	                if (newTranslationManager.getDepartment() != null) existingTranslationManager.setDepartment(newTranslationManager.getDepartment());
	                if (newTranslationManager.getTranslationLimit() != 0) existingTranslationManager.setTranslationLimit(newTranslationManager.getTranslationLimit());
	                if (newTranslationManager.getTranslationUsage() != 0) existingTranslationManager.setTranslationUsage(newTranslationManager.getTranslationUsage());
	                if (newTranslationManager.getTranslationUsageRate() != null) existingTranslationManager.setTranslationUsageRate(newTranslationManager.getTranslationUsageRate());
	                if (newTranslationManager.getRegId() != null) existingTranslationManager.setRegId(newTranslationManager.getRegId());
	                existingTranslationManager.setUpdateDt(LocalDateTime.now());
	
	                // 업데이트된 TranslationManager 저장
	                translationManagerRepository.save(existingTranslationManager);
	            }
        	}else {
        			translationManagerRepository.saveAll(translationManagers);
        	}
        }

        // 수정된 TranslationManager들 반환
        return translationManagerRepository.findAllById(translationManagers.stream().map(TranslationManager::getTranslationId).toList());
    }
}
