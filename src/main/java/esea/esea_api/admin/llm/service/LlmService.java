package esea.esea_api.admin.llm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import esea.esea_api.admin.llm.entities.LlmEntService;
import esea.esea_api.admin.llm.repository.LlmRepository;

@Service
public class LlmService {

    @Autowired
    private LlmRepository llmRepository;
    
    public Page<LlmEntService> searchLlmServiceByName(
            String name,
            int page,
            int size
        ){
        // 페이징 설정
        PageRequest pageRequest = PageRequest.of(page -1, size);
    	
        return llmRepository.findByNameLike(
    		name != null ? name : "",
            pageRequest
        );
    }
    
    public List<LlmEntService> getAllLlmServices() {
        return llmRepository.findAll();  // Fetches all LLM entities from the database
    }
    
    public List<LlmEntService> findAllLlmServices() {
        return llmRepository.findAll();  // This will return all the records from the database
    }
    
    public LlmEntService saveLlmEntService(LlmEntService llmEntService) {
    	return llmRepository.save(llmEntService);
    }
    
    public Optional<LlmEntService> getLlmById(int llmServiceId) {
        return llmRepository.findById(llmServiceId);
    }
    
    public LlmEntService saveLlm(LlmEntService llmEntService) {
        return llmRepository.save(llmEntService);
    }
    
    public void deleteLlmById(int llmServiceId) {
        llmRepository.deleteLlmById(llmServiceId);
    }
    
    public List<LlmEntService> getActiveLlms() {
        return llmRepository.findByStatus("Y");
    }
}

