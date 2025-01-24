package esea.esea_api.translation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.translation.dto.PageResponse;
import esea.esea_api.translation.dto.TranslationFileSearchDTO;
import esea.esea_api.translation.dto.TranslationFileSearchDataDTO;
import esea.esea_api.translation.entities.TranslationFile;
import esea.esea_api.translation.service.TranslationFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "번역 조회 API", description = "번역 조회 API")
@RestController
@RequestMapping("/translate")
public class TranslationFileController {

    private final TranslationFileService translationFileService;
    
    @Autowired
    public TranslationFileController(TranslationFileService translationFileService) {
        this.translationFileService = translationFileService;
    }

    @Operation(
        summary = "날짜별/등록자별 번역파일 조회",
        description = "특정 날짜 범위와 등록자의 번역파일을 페이징하여 조회합니다."
    )
    @PostMapping("/search")
    public ResponseEntity<PageResponse<TranslationFile>> getFilesByDateRangeAndRegId(
            @RequestBody(required = false) TranslationFileSearchDTO searchDTO) {
        
        if (searchDTO == null) {
            searchDTO = new TranslationFileSearchDTO();
        }

        Page<TranslationFile> result = translationFileService.getFilesByDateRangeAndRegId(
            searchDTO.getStartDate(),
            searchDTO.getEndDate(),
            searchDTO.getUserId(),
            searchDTO.getFileNm(),
            searchDTO.getPage(),
            searchDTO.getSize()
        );

        return ResponseEntity.ok(PageResponse.of(result));
    }
    
    @Operation(
            summary = "관리자 DATA관리 (AI번역-로그) 조회",
            description = "관리자 DATA관리 (AI번역-로그) 조회합니다."
        )
        @PostMapping("/searchData")
        public ResponseEntity<PageResponse<TranslationFile>> searchDataList(
                @RequestBody(required = false) TranslationFileSearchDataDTO searchDTO) {
            
            if (searchDTO == null) {
                searchDTO = new TranslationFileSearchDataDTO();
            }

            Page<TranslationFile> result = translationFileService.searchDataList(
                searchDTO.getStartDate(),
                searchDTO.getEndDate(),
                searchDTO.getUserNm(),
                searchDTO.getDeptNm(),
                searchDTO.getPage(),
                searchDTO.getSize()
            );

            return ResponseEntity.ok(PageResponse.of(result));
        }
    
}
