package esea.esea_api.translation.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import esea.esea_api.translation.dto.DeepLResponse;
import esea.esea_api.translation.dto.DeleteTranslationManager;
import esea.esea_api.translation.entities.TranslationFile;
import esea.esea_api.translation.entities.TranslationManager;
import esea.esea_api.translation.service.DeepLService;
import esea.esea_api.translation.service.TranslationFileService;
import esea.esea_api.translation.service.TranslationManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "번역 API", description = "번역 API")
@RestController
@RequestMapping("/translate")
public class TranslateController {
    @Autowired
    private DeepLService deepLService;
    
    @Autowired
    private TranslationFileService translationFileService;
    
    @Autowired
    private TranslationManagerService translationManagerService;
    
    @Operation(summary = "번역 사용량 조회")
    @GetMapping("/usage")
    public ResponseEntity<?> checkUsage(@RequestParam(value="departmentId",required = false) String departmentId) {
        try {
        	Map<String, Object> usageInfo = new HashMap<>();
        	if(StringUtils.isBlank(departmentId)) {
        		usageInfo = new HashMap<String, Object>();
        		usageInfo.put("error", "departmentId is null");
        		return ResponseEntity.ok(usageInfo);
        	}
        	TranslationManager transInfo = translationManagerService.getByDepartmentId(departmentId);
            usageInfo.put("charactersUsed", transInfo.getTranslationUsage());
            usageInfo.put("remainingCharacters", transInfo.getTranslationLimit()-transInfo.getTranslationUsage());
            usageInfo.put("usagePercentage", transInfo.getTranslationUsageRate());
            usageInfo.put("characterLimit", transInfo.getTranslationLimit());
            usageInfo.put("department", transInfo.getDepartment());
            return ResponseEntity.ok(usageInfo);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "등록되지 않은 부서입니다.");
            return ResponseEntity.ok(errorResponse);
        }
    }

    @Operation(summary = "비동기 테스트")
    @PostMapping(value="/async-test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("sourceLang") String sourceLang,
            @RequestParam("targetLang") String targetLang,
            @RequestParam("userId") String userId,
            @RequestParam("userNm") String userNm,
            @RequestParam("departmentId") String departmentId,
            @RequestParam("departmentNm") String departmentNm
    ) {
        

        try {
        	Map<String, String> response = new HashMap<>();
        	
            // 파일 크기로 글자수 계산
        	int characterCount = deepLService.countCharacters(file);
        	
            // 부서 코드로 번역 관리자 정보 조회
        	TranslationManager transInfo = translationManagerService.getByDepartmentId(departmentId);
        	
            // TRANSLATION_LIMIT과 TRANSLATION_USAGE를 사용하여 사용 가능 token수 계산
        	int CHARACTER_LIMIT = transInfo.getTranslationLimit() - transInfo.getTranslationUsage();
        	
            // 최소 글자수는 50000으로 변환
        	if(characterCount < 50000) {
        		characterCount = 50000;
        	}

            // 글자 수 체크
            if (characterCount > CHARACTER_LIMIT) {
            	Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "업로드한 파일의 번역 글자 수 " + characterCount + "가 남은 용량보다 많습니다.");
                return ResponseEntity.ok(errorResponse);
            }
            
            // 사용량 계산
            int translationUsage = transInfo.getTranslationUsage() + characterCount;
            
            // 사용률 계산
            double rate = (double) translationUsage / transInfo.getTranslationLimit() * 100;
            
            // 사용률 계산
            BigDecimal translationUsageRate = BigDecimal.valueOf(rate);
            try {
                // // 번역 파일 업로드
                DeepLResponse fileInfo = deepLService.uploadDocument(file, sourceLang, targetLang, transInfo.getApiKey());

                // 업로드 성공시 사용량 업데이트
                if (fileInfo != null) {
                    translationManagerService.updateTranslationManager(
                        transInfo.getTranslationId(),
                        transInfo.getApiKey(),
                        transInfo.getDepartment(),
                        transInfo.getDepartment_id(),
                        transInfo.getTranslationLimit(),
                        translationUsage,
                        translationUsageRate
                    );
            
                    // 번역 파일 정보저장
                    TranslationFile translationFile = new TranslationFile();
                    translationFile.setOriginalFilename(fileInfo.getOriginalFilename());
                    translationFile.setOriginalFileUrl(fileInfo.getOriginalFileUrl());
                    translationFile.setTranslatedFilename(fileInfo.getTranslatedFilename());
                    translationFile.setTranslatedFileUrl(fileInfo.getTranslatedFileUrl());
                    translationFile.setUpFileUrl("/"+fileInfo.getUpFileUrl());
                    translationFile.setDownFileUrl("/"+fileInfo.getDownFileUrl());
                    translationFile.setLanguage(targetLang);
                    translationFile.setSorcelang(sourceLang);
                    translationFile.setRegId(userId);
                    translationFile.setRegNm(userNm);
                    translationFile.setDeptId(departmentId);
                    translationFile.setDeptNm(departmentNm);
                    translationFile.setTranslted_charcnt(characterCount);
                    translationFile.setRegDt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
        
                    translationFileService.registerTranslationFile(translationFile);
                    
                    // 응답 데이터 저장
                    response.put("upfileName", fileInfo.getOriginalFilename());
                    response.put("downfileName", fileInfo.getTranslatedFilename());
                    response.put("upfileUrl", fileInfo.getOriginalFileUrl());
                    response.put("downfileUrl", fileInfo.getTranslatedFileUrl());
                } else {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "업로드 파일 저장실패 하였습니다.");
                    return ResponseEntity.ok(errorResponse);
                }
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "업로드 번역 실패 하였습니다.\n" + e.getMessage());
                return ResponseEntity.ok(errorResponse);
            }
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @Operation(summary = "번역파일버전")
    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<ResponseEntity<?>> asyncTest(
        @RequestParam("file") MultipartFile file,
        @RequestParam("sourceLang") String sourceLang,
        @RequestParam("targetLang") String targetLang,
        @RequestParam("userId") String userId,
        @RequestParam("userNm") String userNm,
        @RequestParam("departmentId") String departmentId,
        @RequestParam("departmentNm") String departmentNm
    ) {
        return CompletableFuture.supplyAsync(() -> {
            Thread.currentThread().setName("deepLUpload-Thread");
            
            try {
                Map<String, String> response = new HashMap<>();
        	
                // 파일 크기로 글자수 계산
                int characterCount = deepLService.countCharacters(file);
                
                // 부서 코드로 번역 관리자 정보 조회
                TranslationManager transInfo = translationManagerService.getByDepartmentId(departmentId);
                
                // TRANSLATION_LIMIT과 TRANSLATION_USAGE를 사용하여 사용 가능 token수 계산
                int CHARACTER_LIMIT = transInfo.getTranslationLimit() - transInfo.getTranslationUsage();
                
                // 최소 글자수는 50000으로 변환
                if(characterCount < 50000) {
                    characterCount = 50000;
                }

                // 글자 수 체크
                if (characterCount > CHARACTER_LIMIT) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "업로드한 파일의 번역 글자 수 " + characterCount + "가 남은 용량보다 많습니다.");
                    return ResponseEntity.ok(errorResponse);
                }
                
                // 사용량 계산
                int translationUsage = transInfo.getTranslationUsage() + characterCount;
                
                // 사용률 계산
                double rate = (double) translationUsage / transInfo.getTranslationLimit() * 100;
                
                // 사용률 계산
                BigDecimal translationUsageRate = BigDecimal.valueOf(rate);

                try {
                    // 번역 파일 업로드
                    DeepLResponse fileInfo = deepLService.uploadDocument(file, sourceLang, targetLang, transInfo.getApiKey());

                    // 업로드 성공시 사용량 업데이트
                    if (fileInfo != null) {
                        translationManagerService.updateTranslationManager(
                            transInfo.getTranslationId(),
                            transInfo.getApiKey(),
                            transInfo.getDepartment(),
                            transInfo.getDepartment_id(),
                            transInfo.getTranslationLimit(),
                            translationUsage,
                            translationUsageRate
                        );
                
                        // 번역 파일 정보저장
                        TranslationFile translationFile = new TranslationFile();
                        translationFile.setOriginalFilename(fileInfo.getOriginalFilename());
                        translationFile.setOriginalFileUrl(fileInfo.getOriginalFileUrl());
                        translationFile.setTranslatedFilename(fileInfo.getTranslatedFilename());
                        translationFile.setTranslatedFileUrl(fileInfo.getTranslatedFileUrl());
                        translationFile.setUpFileUrl("/"+fileInfo.getUpFileUrl());
                        translationFile.setDownFileUrl("/"+fileInfo.getDownFileUrl());
                        translationFile.setLanguage(targetLang);
                        translationFile.setSorcelang(sourceLang);
                        translationFile.setRegId(userId);
                        translationFile.setRegNm(userNm);
                        translationFile.setDeptId(departmentId);
                        translationFile.setDeptNm(departmentNm);
                        translationFile.setTranslted_charcnt(characterCount);
                        translationFile.setRegDt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            
                        translationFileService.registerTranslationFile(translationFile);
                        
                        // 응답 데이터 저장
                        response.put("upfileName", fileInfo.getOriginalFilename());
                        response.put("downfileName", fileInfo.getTranslatedFilename());
                        response.put("upfileUrl", fileInfo.getOriginalFileUrl());
                        response.put("downfileUrl", fileInfo.getTranslatedFileUrl());
                    } else {
                        Map<String, String> errorResponse = new HashMap<>();
                        errorResponse.put("error", "업로드 파일 저장실패 하였습니다.");
                        return ResponseEntity.ok(errorResponse);
                    }
                } catch (Exception e) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "업로드 번역 실패 하였습니다.\n" + e.getMessage());
                    return ResponseEntity.ok(errorResponse);
                }
                
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "등록되지 않은 부서입니다.");
                return ResponseEntity.ok(errorResponse);
            }
        });
    }
    
    @Operation(summary = "번역 apikey 조회")
    @GetMapping("apiKeySearch")
    public List<TranslationManager> getAllTranslationManagers() {
        return translationManagerService.getAllTranslationManagers();
    }
    
    @Operation(summary = "번역 apikey 등록")
    // 등록 (여러 항목을 한 번에)
    @PostMapping("apiKeyInsert")
    public List<TranslationManager> createTranslationManagers(@RequestBody List<TranslationManager> translationManagers) {
        return translationManagerService.updateTranslationManagers(translationManagers);
    }
    
    @Operation(summary = "번역 apikey 삭제")
    @PostMapping("/apiKeyDelete")
    public ResponseEntity<String> deleteTranslationManagers(@RequestBody DeleteTranslationManager deleteTranslationManager) {
        	translationManagerService.deleteTranslationManagers(deleteTranslationManager);
            return ResponseEntity.noContent().build();
    }
}
