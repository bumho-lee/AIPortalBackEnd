package esea.esea_api.admin.filtering.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import esea.esea_api.admin.filtering.dto.DeleteRequestDto;
import esea.esea_api.admin.filtering.entities.FilteringService;
import esea.esea_api.admin.filtering.repositories.FilteringServiceRepository;
import esea.esea_api.admin.role.entities.RoleManager;

@Service
public class FilteringServiceService {
    @Autowired
    private FilteringServiceRepository repository;

    // 여러 레코드 등록
    public List<FilteringService> createFilteringServices(List<FilteringService> filteringServices) {
        return repository.saveAll(filteringServices); // JPA의 Batch Insert 처리
    }
    
    public Page<FilteringService> searchByKeyword(
    		String keyword,
            int page,
            int size
        ){
        // 페이징 설정
        PageRequest pageRequest = PageRequest.of(page -1, size);
    	
        return repository.findByKeywordLike(
        		keyword != null ? keyword : "",
            pageRequest
        );
    }

    // 수정
    public List<FilteringService> updateFilteringServices(List<FilteringService> filteringServices) {
        return filteringServices.stream()
                .map(filteringService -> repository.findById(filteringService.getFilteringServiceId())
                        .map(existingService -> {
                            existingService.setKeyword(filteringService.getKeyword());
                            existingService.setReason(filteringService.getReason());
                            existingService.setUseYn(filteringService.getUseYn());
                            existingService.setUserId(filteringService.getUserId());
                            existingService.setUpdateDt(LocalDateTime.now());
                            return repository.save(existingService);
                        })
                        .orElseThrow(() -> new RuntimeException("FilteringService not found with ID: "
                                + filteringService.getFilteringServiceId())))
                .toList();
    }

    // 삭제
    public void deleteFilteringServices(DeleteRequestDto deleteRequestDto) {
        if (deleteRequestDto.getIds() == null || deleteRequestDto.getIds().isEmpty()) {
            throw new IllegalArgumentException("삭제할 ID 목록이 비어 있습니다.");
        }
        repository.deleteAllById(deleteRequestDto.getIds());
    }
}
