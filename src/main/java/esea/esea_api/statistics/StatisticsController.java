package esea.esea_api.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import esea.esea_api.statistics.dto.CollectionListRequestDto;
import esea.esea_api.statistics.dto.CollectionListResponseDto;
import esea.esea_api.statistics.dto.ConversationLogRequestDto;
import esea.esea_api.statistics.dto.ConversationLogResponseDto;
import esea.esea_api.statistics.dto.UserLlmUsageRequestDto;
import esea.esea_api.statistics.dto.UserLlmUsageExcelRequestDto;
import esea.esea_api.statistics.dto.UserLlmUsageResponseDto;
import esea.esea_api.statistics.dto.CollectionManagementRequestDto;
import esea.esea_api.statistics.dto.CollectionDataRequestDto;
import esea.esea_api.statistics.dto.CollectionDataLogResponseDto;
import esea.esea_api.statistics.dto.CollectionManagementUpdateRequestDto;
import esea.esea_api.statistics.dto.ConversationLogExcelRequestDto;
import esea.esea_api.statistics.dto.CollectionManagementDetailRequestDto;
import esea.esea_api.statistics.dto.CollectionDeleteRequestDto;
import esea.esea_api.statistics.dto.CollectionDetailResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Tag(name = "통계", description = "통계 내용")
@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private DataCollectionService dataCollectionService;

    @Operation(summary = "유저별 LLM 사용량", description = "유저별 LLM 사용량 갖고오기")
    @PostMapping("/user-usage")
    public UserLlmUsageResponseDto getUserUsage(@RequestBody UserLlmUsageRequestDto requestDto) {
        return statisticsService.getUserUsage(requestDto);
    }

    @Operation(summary = "유저별 LLM 사용량 엑셀 다운로드", description = "유저별 LLM 사용량 엑셀 다운로드")
    @GetMapping("/user-usage-excel")
    public ResponseEntity<byte[]> downloadUserUsageExcel(@ModelAttribute UserLlmUsageExcelRequestDto requestDto) {
        byte[] excelFile = statisticsService.downloadUserUsageExcel(requestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
            .filename("유저별 LLM 사용량.xlsx", StandardCharsets.UTF_8)
            .build());
        
        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
    }

    @Operation(summary = "유저별 대화 로그  ", description = "유저별 대화 로그 갖고오기")
    @PostMapping("/user-conversation-log")
    public ConversationLogResponseDto getUserConversationLog(@RequestBody ConversationLogRequestDto requestDto) {
        return statisticsService.getUserConversationLog(requestDto);
    }

    @Operation(summary = "유저별 대화 로그 엑셀 다운로드", description = "유저별 대화 로그 엑셀 다운로드")
    @GetMapping("/user-conversation-log-excel")
    public ResponseEntity<byte[]> downloadUserConversationLogExcel(@ModelAttribute ConversationLogExcelRequestDto requestDto) {
        byte[] excelFile = statisticsService.downloadUserConversationLogExcel(requestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
            .filename("유저별 대화 로그.xlsx", StandardCharsets.UTF_8)
            .build());
        
        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
    }    

    // 수집 Data 관리 생성
    @Operation(summary = "수집 Data 관리 생성", description = "수집 Data 관리 생성")
    @PostMapping("/collection-management-create")
    public Boolean createCollectionManagement(@RequestBody CollectionManagementRequestDto body) {
        return dataCollectionService.createCollection(body);
    }

    // 수집 Data 관리 수정
    @Operation(summary = "수집 Data 관리 수정", description = "수집 Data 관리 수정")
    @PostMapping("/collection-management-update")
    public Boolean updateCollectionManagement(@RequestBody CollectionManagementUpdateRequestDto body) {
        return dataCollectionService.updateCollection(body);
    }

    // 수집 Data 관리 목록
    @Operation(summary = "수집 Data 관리 목록", description = "수집 Data 관리 목록")
    @PostMapping("/collection-management-list")
    public CollectionListResponseDto getCollectionList(@RequestBody CollectionListRequestDto body) {
        return dataCollectionService.getCollectionList(body);
    }

    // 수집 Data 관리 상세
    @Operation(summary = "수집 Data 관리 상세", description = "수집 Data 관리 상세")
    @PostMapping("/collection-management-detail")
    public CollectionDetailResponseDto getCollectionManagementDetail(@RequestBody CollectionManagementDetailRequestDto body) {
        return dataCollectionService.getCollectionManagementDetail(body);
    }

    // 수집 Data 등록
    @Operation(summary = "수집 Data 등록", description = "수집 Data 등록")
    @PostMapping(value = "/collection-log-create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> registerCollection(
        @Parameter(description = "업로드할 파일들", required = true) @RequestPart(value = "file") MultipartFile file,
        @Parameter(description = "name", required = true ) @RequestPart("name") String name,
        @Parameter(description = "collectionId", required = true) @RequestPart("collectionId") String collectionId,
        @Parameter(description = "userId", required = true) @RequestPart("userId") String userId
    ) {
        return dataCollectionService.registerCollection(file, name, collectionId, userId);
    }

    // 수집 Data 등록 목록
    @Operation(summary = "수집 Data 등록 목록", description = "수집 Data 등록 목록")
    @PostMapping("/collection-log-list")
    public CollectionDataLogResponseDto getCollectionLog(@RequestBody CollectionDataRequestDto requestDto) {
        return dataCollectionService.getCollectionLog(requestDto);
    }

    // 수집 Data 등록 삭제
    @Operation(summary = "수집 Data 등록 삭제", description = "수집 Data 삭제")
    @PostMapping("/collection-log-delete")
    public Boolean deleteCollection(@RequestBody CollectionDeleteRequestDto body) {
        return dataCollectionService.deleteCollection(body);
    }

}
