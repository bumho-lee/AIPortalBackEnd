package esea.esea_api.admin.banner.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import esea.esea_api.admin.banner.entities.BannerEntService;
import esea.esea_api.admin.banner.service.BannerService;
import esea.esea_api.admin.llm.dto.LlmServiceRequest;
import esea.esea_api.translation.dto.PageResponse;
import esea.esea_api.translation.service.DeepLService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "BANNER API", description = "BANNER API")
@RestController
@RequestMapping("/banner")
public class BannerController {
	@Autowired
	private BannerService bannerService;
    @Autowired
    private DeepLService deepLService;
 
    @Operation(summary = "BANNER 조회", description = "BANNER 조회")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<BannerEntService>> searchBannerServiceByName(
    		@RequestParam(value = "name",required = false) String name,
    		@RequestParam(value = "page",required = false) int page,
    		@RequestParam(value = "size",required = false) int size) {
    	
        Page<BannerEntService> result = bannerService.searchBannerServiceByName(name, page, size);
        
        return ResponseEntity.ok(PageResponse.of(result));
    }
    
    @Operation(summary = "BANNER 작성", description = "BANNER 작성 (다중 파일 업로드)")
    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<BannerEntService>> registerBannerEntServices(
    	    @Parameter(description = "업로드할 파일들", required = true) @RequestPart(value = "files") List<MultipartFile> files,
    	    @Parameter(description = "name", required = true ) @RequestPart("names") String names,
    	    @Parameter(description = "useYn", required = true) @RequestPart("useYns") String useYns) throws UnsupportedEncodingException {

		  String nameText = new String(names.getBytes(), "UTF-8");
		  String[] nameResult = nameText.split(",");
		  String useYnsText = new String(useYns.getBytes(), "UTF-8");
		  String[] useYnsResult = nameText.split(",");

        List<BannerEntService> savedBanners = new ArrayList<>();

        try {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String name = nameResult[i];
                String useYn = useYnsResult[i];

                // 시스템의 임시 디렉토리 경로 가져오기
                String tempDir = System.getProperty("java.io.tmpdir");
                String filePath = tempDir + File.separator + file.getOriginalFilename();
                File destinationFile = new File(filePath);
                file.transferTo(destinationFile);
                
                LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                String formattedDateTime = now.format(formatter);

                String bucketName = "ddi-ai-web-bucket";
                String s3UploadPath = "banner/"+formattedDateTime+"_"+file.getOriginalFilename();
                String localFilePath = destinationFile.getAbsolutePath();
                
                // S3 파일 업로드
                String uploadResponse = deepLService.uploadFile(bucketName, s3UploadPath, localFilePath);

                // 배너 엔티티 생성 및 설정
                BannerEntService bannerEntService = new BannerEntService();
                bannerEntService.setName(name);
                bannerEntService.setFileName(file.getOriginalFilename());
                bannerEntService.setFilePath(uploadResponse);
                bannerEntService.setUseYn(useYn);
                bannerEntService.setUserId("test1234");
                bannerEntService.setRegDt(LocalDateTime.now());

                // 저장
                BannerEntService savedBanner = bannerService.saveBannerEntService(bannerEntService);
                savedBanners.add(savedBanner);

                // 임시 파일 삭제
                destinationFile.delete();
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedBanners);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @Operation(summary = "BANNER 상세조회", description = "BANNER 상세조회")
    @GetMapping("/detail")
    public Optional<BannerEntService> getBannerById(@RequestParam(value = "bannerServiceId", required = false) int bannerServiceId) {
        return bannerService.getBannerById(bannerServiceId);
    }
    
    @Operation(summary = "BANNER 수정", description = "여러 배너 변경된 값만 수정")
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<BannerEntService>> updateBannersWithFiles(
            @Parameter(description = "bannerServiceIds", required = true ) @RequestPart("bannerServiceIds") String bannerServiceIds,
    	    @Parameter(description = "업로드할 파일들", required = true) @RequestPart(value = "files") List<MultipartFile> files,
    	    @Parameter(description = "name", required = true ) @RequestPart("names") String names,
    	    @Parameter(description = "useYn", required = true) @RequestPart("useYns") String useYns) throws UnsupportedEncodingException {

  	  String bannerServiceIdsText = new String(bannerServiceIds.getBytes(), "UTF-8");
  	  String[] bannerServiceIdsResult = bannerServiceIdsText.split(",");
	  String nameText = new String(names.getBytes(), "UTF-8");
	  String[] nameResult = nameText.split(",");
	  String useYnsText = new String(useYns.getBytes(), "UTF-8");
	  String[] useYnsResult = nameText.split(",");
    	
        List<BannerEntService> updatedBanners = new ArrayList<>();
        List<File> tempFiles = new ArrayList<>();

        try {
            for (int i = 0; i < files.size(); i++) {
                Integer bannerId = Integer.parseInt(bannerServiceIdsResult[i]);
                Optional<BannerEntService> bannerOpt = bannerService.getBannerById(bannerId);

                if (bannerOpt.isPresent()) {
                    BannerEntService banner = bannerOpt.get();

                    // 이름 업데이트
                    if (names != null && nameResult[i] != null) {
                        banner.setName(nameResult[i]);
                    }

                    // 사용여부 업데이트
                    if (useYns != null && useYnsResult[i] != null) {
                        banner.setUseYn(useYnsResult[i]);
                    }

                    // 시간 업데이트
                    banner.setUpdateDt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

                    // 파일 업데이트
                    if (files != null && files.get(i) != null && !files.get(i).isEmpty()) {
                        MultipartFile file = files.get(i);

                        // 새 파일 업로드
                        String tempDir = System.getProperty("java.io.tmpdir");
                        String filePath = tempDir + File.separator + file.getOriginalFilename();
                        File tempFile = new File(filePath);
                        tempFiles.add(tempFile);
                        file.transferTo(tempFile);
                        
                        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
                        String formattedDateTime = now.format(formatter);

                        String bucketName = "ddi-ai-web-bucket";
                        String s3UploadPath = "banner/"+formattedDateTime+"_"+file.getOriginalFilename();
                        String localFilePath = tempFile.getAbsolutePath();
                        
                        // 파일 업로드
                        String uploadResponse = deepLService.uploadFile(bucketName, s3UploadPath, localFilePath);

                        banner.setFileName(file.getOriginalFilename());
                        banner.setFilePath(uploadResponse);
                    }

                    // 배너 저장
                    BannerEntService savedBanner = bannerService.saveBanner(banner);
                    updatedBanners.add(savedBanner);
                }
            }

            return ResponseEntity.ok(updatedBanners);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            // 임시 파일 삭제
            for (File tempFile : tempFiles) {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
    }
    
    @Operation(summary = "BANNER 삭제", description = "다중 BANNER 삭제")
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteBanners(@RequestBody List<Integer> bannerServiceIds) {
        Map<String, Object> response = bannerService.deleteBanners(bannerServiceIds);
        return ResponseEntity.ok(response);
    }
}
