package esea.esea_api.admin.llm.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import esea.esea_api.admin.llm.dto.LlmServiceRequest;
import esea.esea_api.admin.llm.entities.LlmEntService;
import esea.esea_api.admin.llm.service.LlmService;
import esea.esea_api.translation.dto.PageResponse;
import esea.esea_api.translation.service.DeepLService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "LLM API", description = "LLM API")
@RestController
@RequestMapping("/llm")
public class LlmController {

    @Autowired
    private LlmService llmService;
    @Autowired
    private DeepLService deepLService;
 
    @Operation(summary = "LLM 조회", description = "LLM 조회")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<LlmEntService>> searchLlmServiceByName(
    		@RequestParam(value = "name",required = false) String name,
    		@RequestParam(value = "page",required = false) int page,
    		@RequestParam(value = "size",required = false) int size) {
    	
        Page<LlmEntService> result = llmService.searchLlmServiceByName(name, page, size);
        
        return ResponseEntity.ok(PageResponse.of(result));
    }
    
    @Operation(summary = "LLM 전체조회", description = "LLM 전체조회")
    @GetMapping("/searchAll")
    public ResponseEntity<List<LlmEntService>> searchLlmService() {
        // Fetch all LLMs without conditions or pagination
        List<LlmEntService> result = llmService.getAllLlmServices();  // Adjusted method for all LLMs without conditions
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "LLM ACTIVE", description = "LLM ACTIVE")
    @GetMapping("/active")
    public ResponseEntity<List<LlmEntService>> getActiveLlms() {
        List<LlmEntService> activeLlms = llmService.getActiveLlms();
        return ResponseEntity.ok(activeLlms);
    }
    
    @Operation(
    	    summary = "LLM 작성",
    	    description = "LLM 서비스 등록 (MultipartForm 형식)"
    	)
    @PostMapping(value= "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LlmEntService> registerLlmEntService(
    		 @Parameter(description = "업로드할 파일", required = true, schema = @Schema(type = "string", format = "binary")) @RequestPart(value = "file") MultipartFile file,
    		 @Parameter(description = "LLM 서비스 이름",required = true,example = "GPT-4")@RequestPart(value = "name") String name,
    		 @Parameter(description = "서비스 키",required = false,example = "sk-123456789")@RequestPart(value = "serviceKey") String serviceKey,
    		 @Parameter(description = "서비스 상태",required = true,example = "ACTIVE")@RequestPart(value = "status") String status,
    		 @Parameter(description = "메모",required = true,example = "대용량 언어 모델 서비스")@RequestPart(value = "note") String note) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            // 시스템의 임시 디렉토리 경로 가져오기
            String tempDir = System.getProperty("java.io.tmpdir");
            String filePath = tempDir + File.separator + file.getOriginalFilename();
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);
            
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String formattedDateTime = now.format(formatter);
            
            String bucketName = "ddi-ai-web-bucket";
            String s3UploadPath = "llm/"+formattedDateTime+"_"+file.getOriginalFilename();
            String localFilePath = destinationFile.getAbsolutePath();
            // 파일 업로드
            String uploadResponse = deepLService.uploadFile(bucketName, s3UploadPath, localFilePath);

            LlmEntService llmEntService = new LlmEntService();
            
            llmEntService.setIconPath(uploadResponse);
            llmEntService.setIconUrl("/"+s3UploadPath);
            llmEntService.setName(name);
            llmEntService.setServiceKey(serviceKey);
            llmEntService.setStatus(status);
            llmEntService.setNote(note);
            llmEntService.setRegDt(LocalDateTime.now());
            
            LlmEntService savedLlmEntService = llmService.saveLlmEntService(llmEntService);;
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLlmEntService);

        } catch (Exception e) {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "LLM 수정", description = "변경된 값만 수정")
    @PostMapping(value="/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LlmEntService> updateLlmWithFile(
   		 @Parameter(description = "업로드할 파일", required = false,  schema = @Schema(type = "string", format = "binary")) @RequestPart(value = "file", required = false) MultipartFile file,
   		 @Parameter(description = "LLM 서비스 ID",example = "GPT-4")@RequestPart(value = "llmServiceId") String llmServiceId,
   		 @Parameter(description = "LLM 서비스 이름",example = "GPT-4")@RequestPart(value = "name", required = false) String name,
   		 @Parameter(description = "서비스 키",example = "sk-123456789")@RequestPart(value = "serviceKey", required = false) String serviceKey,
   		 @Parameter(description = "서비스 상태",example = "ACTIVE")@RequestPart(value = "status", required = false) String status,
   		 @Parameter(description = "메모",example = "대용량 언어 모델 서비스")@RequestPart(value = "note", required = false) String note) throws IllegalStateException, IOException {
    	
    	Optional<LlmEntService> llmOpt = llmService.getLlmById(Integer.parseInt(llmServiceId));
    	
    	LlmEntService updatedLlm = new LlmEntService();

    	if (llmOpt.isPresent()) {
    		
    		LlmEntService llm = llmOpt.get();
    		
    		// Update fields if provided
            if (name != null) llm.setName(name);
            if (serviceKey != null) llm.setServiceKey(serviceKey);
            if (status != null) llm.setStatus(status);
            if (note != null) llm.setNote(note);
            llm.setUpdateDt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            
            if (file != null && !file.isEmpty()) {
                // 시스템의 임시 디렉토리 경로 가져오기
                String tempDir = System.getProperty("java.io.tmpdir");
                String filePath = tempDir + File.separator + file.getOriginalFilename();
                File destinationFile = new File(filePath);
                file.transferTo(destinationFile);
                
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String formattedDateTime = now.format(formatter);
                
                String bucketName = "ddi-ai-web-bucket";
                String s3UploadPath = "llm/"+formattedDateTime+"_"+file.getOriginalFilename();
                String localFilePath = destinationFile.getAbsolutePath();
                // 파일 업로드
                String uploadResponse = deepLService.uploadFile(bucketName, s3UploadPath, localFilePath);
                
                llm.setIconPath(uploadResponse);
            }
            updatedLlm = llmService.saveLlm(llm);
    	}
    	return ResponseEntity.ok(updatedLlm);
    }
    
    
    @Operation(summary = "LLM 상세조회", description = "LLM 상세조회")
    @GetMapping("/detail")
    public Optional<LlmEntService> getLlmById(@RequestParam(value = "llmServiceId", required = false) int llmServiceId) {
        return llmService.getLlmById(llmServiceId);
    }
    
    @Operation(
    	    summary = "LLM 삭제", 
    	    description = "LLM 삭제"
    	)
    @PostMapping("/delete")
    public ResponseEntity<String> deleteLlm(@RequestBody LlmServiceRequest llmServiceRequest) {
        llmService.deleteLlmById(llmServiceRequest.getLlmServiceId());
        return ResponseEntity.ok("LLM Service deleted successfully.");
    }
}
