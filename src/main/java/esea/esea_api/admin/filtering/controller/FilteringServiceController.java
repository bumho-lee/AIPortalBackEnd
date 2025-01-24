package esea.esea_api.admin.filtering.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.admin.filtering.dto.DeleteRequestDto;
import esea.esea_api.admin.filtering.entities.FilteringService;
import esea.esea_api.admin.filtering.service.FilteringServiceService;
import esea.esea_api.translation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "필터링 API", description = "필터링 API")
@RestController
@RequestMapping("/filtering")
public class FilteringServiceController {

    @Autowired
    private FilteringServiceService service;

    // 여러 레코드 등록
    @Operation(summary = "필터링 등록", description = "필터링 등록")
    @PostMapping("/insert")
    public ResponseEntity<List<FilteringService>> createFilteringServices(
            @RequestBody List<FilteringService> filteringServices) {
        // filteringServices 리스트의 각 FilteringService 객체에 REG_DT를 설정
        filteringServices.forEach(service -> service.setRegDt(LocalDateTime.now()));
    	
        return ResponseEntity.ok(service.createFilteringServices(filteringServices));
    }

    @Operation(summary = "필터링 조회", description = "필터링 조회")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<FilteringService>> searchByKeyword(
    		@RequestParam(value = "keyword",required = false) String keyword,
    		@RequestParam(value = "page",required = false) int page,
    		@RequestParam(value = "size",required = false) int size
    		) {
    	
    	Page<FilteringService> result = service.searchByKeyword(keyword, page, size);
    	
        return ResponseEntity.ok(PageResponse.of(result));
    }

    // 수정
    @Operation(summary = "필터링 수정", description = "필터링 수정")
    @PostMapping("/update")
    public ResponseEntity<List<FilteringService>> updateFilteringServices(
            @RequestBody List<FilteringService> filteringServices) {
        return ResponseEntity.ok(service.updateFilteringServices(filteringServices));
    }

    // 삭제
    @Operation(summary = "필터링 삭제", description = "필터링 삭제")
    @PostMapping("/delete")
    public ResponseEntity<Void> deleteFilteringServices(@RequestBody DeleteRequestDto deleteRequestDto) {
        service.deleteFilteringServices(deleteRequestDto);
        return ResponseEntity.noContent().build();
    }
}
