package esea.esea_api.gap;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import esea.esea_api.gap.dto.law_gap_detail.LawGapDetailRequestDto;
import esea.esea_api.gap.dto.law_gap_detail.LawGapDetailResponseDto;
import esea.esea_api.gap.dto.LawGapCreateRequestDto;
import esea.esea_api.gap.dto.LawGapExcelRequestDto;
import esea.esea_api.gap.dto.LawGapFeedbackResponseDto;
import esea.esea_api.gap.dto.LawGapListResponseDto;
import esea.esea_api.gap.dto.LawGapRequestDto;
import esea.esea_api.gap.dto.law_gap_detail.LawGapFeedbackRequestDto;
import esea.esea_api.gap.dto.LawGapReviewerRequestDto;
import esea.esea_api.gap.dto.LawGapManagerRequestDto;
import esea.esea_api.gap.dto.FeedbackListRequestDto;

import java.nio.charset.StandardCharsets;

@Tag(name = "갭분석 API", description = "갭분석 Rest API 목록")
@RestController
@RequestMapping("/gap")
public class GapController {
    @Autowired
    private GapService gapService;

    @Operation(summary = "갭분석 목록 갖고오기", description = "갭분석 목록 갖고오기")
    @GetMapping("/list")
    public LawGapListResponseDto getGap(@ModelAttribute LawGapRequestDto requestDto) {
        return gapService.searchLawGaps(requestDto);
    }

    @Operation(summary = "갭분석 목록 엑셀 다운로드", description = "갭분석 목록을 엑셀 파일로 다운로드")
    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> downloadExcel(@ModelAttribute LawGapExcelRequestDto requestDto) {
        byte[] excelFile = gapService.generateExcelFile(requestDto);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment()
            .filename("gap-analysis.xlsx", StandardCharsets.UTF_8)
            .build());
        
        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
    }

    @Operation(summary = "갭분석 상세정보", description = "갭분석 상세정보 갖고오기")
    @PostMapping("/detail")
    public LawGapDetailResponseDto getGapDetail(@RequestBody LawGapDetailRequestDto body) {
        return gapService.getGapDetail(body);
    }

    @Operation(summary = "분석 권한 체크", description = "검토자 선정 권한 여부 확인")
    @PostMapping("/manager")
    public ResponseEntity<Map<String, String>> checkGapManager(@RequestBody LawGapManagerRequestDto body) {
        String userId = body.getUserId();

        Boolean result = gapService.checkGapManager(userId);

        Map<String, String> response = new HashMap<>();
        response.put("managerYn", result ? "Y" : "N");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "갭분석 검토자 선정", description = "")
    @PostMapping("/reviewer")
    public ResponseEntity<Map<String, String>> setReviewer(@RequestBody LawGapReviewerRequestDto body) {
        return gapService.setReviewer(body);
    }

    @Operation(summary = "갭분석 피드백", description = "갭분석 피드백 하기")
    @PostMapping("/feedback")
    public Boolean feedbackGap(@RequestBody LawGapFeedbackRequestDto body) {
        if("NONE".equals(body.getFeedback())) {
            gapService.deleteFeedback(body);
        } else {
            gapService.feedbackGap(body);
        }

        return true;
    }

    @Operation(summary = "갭분석 피드백 목록 갖고오기", description = "갭분석 채팅 목록 갖고오기")
    @GetMapping("/feedback/list")
    public List<LawGapFeedbackResponseDto> getFeedbackList(@ModelAttribute FeedbackListRequestDto body) {
        return gapService.getFeedbackList(body);
    }

    @Operation(summary = "갭분석 채팅 만들기", description = "갭분석 채팅 만들기")
    @PostMapping("/gap-chat")
    public Map<String, String> createChat(@RequestBody LawGapCreateRequestDto body) {
        return gapService.createChat(body);
    }
}
