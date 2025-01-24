package esea.esea_api.gap;

import esea.org.service.NeoOrgWsProxy;
import esea.org.vo.OrgUserVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import esea.esea_api.admin.role.repository.RoleManagerRepository;
import esea.esea_api.chat.dto.ConversationResponseDto;
import esea.esea_api.chat.dto.SourceResponseDto;
import esea.esea_api.dynamodb_repository.ChatHistoryRepository;

import esea.esea_api.gap.dto.LawGapResponseDTO;
import esea.esea_api.gap.dto.law_gap_detail.LawGapDetailRequestDto;
import esea.esea_api.gap.dto.law_gap_detail.LawGapDetailResponseDto;
import esea.esea_api.gap.dto.law_gap_detail.LawGapFeedbackRequestDto;
import esea.esea_api.gap.dto.LawGapRequestDto;
import esea.esea_api.gap.dto.LawGapListResponseDto;
import esea.esea_api.gap.dto.LawGapCreateRequestDto;
import esea.esea_api.gap.dto.LawGapExcelRequestDto;
import esea.esea_api.gap.dto.LawGapReviewerRequestDto;
import esea.esea_api.gap.dto.FeedbackListRequestDto;
import esea.esea_api.gap.dto.LawGapFeedbackResponseDto;
import esea.esea_api.gap.projections.LawGapProjection;

import esea.esea_api.entities.LawGap;
import esea.esea_api.entities.LawGapFeedback;
import esea.esea_api.entities.Knowledge;
import esea.esea_api.admin.role.entities.RoleManager;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.data.domain.Page;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import esea.esea_api.repositories.LawGapFeedBackRepository;
import esea.esea_api.repositories.KnowledgeRepository;

import esea.esea_api.util.SourceProcessor;

import java.rmi.RemoteException;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.core.env.Environment;

@Service
public class GapService {
    private final Environment env;
    private final boolean AI_NEOSLO_ENABLED;

    public GapService(Environment env) {
        this.env = env;
        this.AI_NEOSLO_ENABLED = Boolean.parseBoolean(env.getProperty("neoslo.enabled", "true"));
    }

    @Autowired
    private GapRepository gapRepository;

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @Autowired
    private LawGapFeedBackRepository feedbackRepository;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private SourceProcessor sourceProcessor;

    @Autowired
    private RoleManagerRepository roleManagerRepository;

    public LawGapListResponseDto searchLawGaps(LawGapRequestDto requestDto) {
        PageRequest pageRequest = PageRequest.of(requestDto.getPage(), requestDto.getSize());
        
        // 검색 조건 설정
        OffsetDateTime startDate = null;
        OffsetDateTime endDate = null;

        // 날짜 유효성 검사
        if ((requestDto.getStartDate() != null && requestDto.getEndDate() == null) ||
            (requestDto.getStartDate() == null && requestDto.getEndDate() != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작일과 종료일 모두 입력해야 합니다.");
        }

        // 날짜 조건이 있는 경우에만 변환
        if (requestDto.getStartDate() != null && requestDto.getEndDate() != null) {
            LocalDate start = LocalDate.parse(requestDto.getStartDate(),
                    DateTimeFormatter.ofPattern("yyyyMMdd"));
            LocalDate end = LocalDate.parse(requestDto.getEndDate(),
                    DateTimeFormatter.ofPattern("yyyyMMdd"));

            startDate = start.atStartOfDay()
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toOffsetDateTime();

            endDate = end.atTime(23, 59, 59)
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toOffsetDateTime();
        }

        Page<LawGapProjection> gapPage = gapRepository.searchLawGaps(startDate, endDate, requestDto.getKeyword(), requestDto.getFeedbackYn(), requestDto.getManagerName(), requestDto.getReviewerName(), pageRequest);

        // 결과 설정
        final NeoOrgWsProxy proxy = AI_NEOSLO_ENABLED ? new NeoOrgWsProxy() : null;

        LawGapListResponseDto result = new LawGapListResponseDto();
        result.setContent(gapPage.getContent().stream()
                .map(gap -> {
                    LawGapResponseDTO dto = new LawGapResponseDTO(gap);

                    // 좋아요 누른 유저 처리
                    List<String> feedbackUserList = new ArrayList<>();
                    if(proxy != null) {
                        if (gap.getFeedbackUserIds() != null && !gap.getFeedbackUserIds().isEmpty()) {
                            for(String feedbackUserId : gap.getFeedbackUserIds().split(",")) {
                                String userName = "";
                                try {
                                    OrgUserVO[] userList = proxy.searchUserByUserId(feedbackUserId);
                                    if(userList != null && userList.length > 0) {
                                        userName = userList[0].getUserName();
                                    }
                                } catch (RemoteException e) {
                                    System.err.println("사용자 조회 중 오류 발생: " + e.getMessage());
                                }

                                if(!userName.isEmpty()) {
                                    feedbackUserList.add(userName + "(" + feedbackUserId + ")");
                                } else {
                                    feedbackUserList.add(feedbackUserId);
                                }
                            }
                        }
                    }
                    dto.setFeedbackUsers(feedbackUserList);
                    
                    try {
                        // 분석 결과가 있는 경우 연관 사규 처리
                        if (gap.getAnalysisResult() != null && !gap.getAnalysisResult().isEmpty()) {
                            JsonNode analysisResult = new ObjectMapper().readTree(gap.getAnalysisResult());
                            
                            if(analysisResult.get("affected_company_rules") != null) {
                                String sourceString = analysisResult.get("affected_company_rules").asText();
                                
                                List<String> sources = SourceProcessor.processSourceString(sourceString);
                                List<SourceResponseDto> sourceResponseDtos = sourceProcessor.analyzePattern(sources, "INDEX_FILE_PATH");

                                if (sourceResponseDtos != null && !sourceResponseDtos.isEmpty()) {
                                    dto.setRegulations(sourceResponseDtos);
                                }
                            }
                        }
                        
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    return dto;
                })
                .collect(Collectors.toList()));
        result.setTotalCount((int) gapPage.getTotalElements());

        return result;
    }

    // 분석 권한 체크
    public Boolean checkGapManager(String userId) {
        List<RoleManager> roleManager = roleManagerRepository.findByUserIdAndRoleId(userId, 6);

        if (roleManager == null || roleManager.isEmpty()) {
            return false;
        }

        return true;
    }

    // 검토자 선정
    @Transactional
    public ResponseEntity<Map<String, String>> setReviewer(LawGapReviewerRequestDto body) {
        if(body.getUserId().equals("NONE")) {
            gapRepository.setReviewer(Integer.parseInt(body.getGapId()), null);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("success", "검토자 선정 완료");
            return ResponseEntity.ok(successResponse);
        }

        if(!AI_NEOSLO_ENABLED) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "slo를 사용하지 못합니다.");
            return ResponseEntity.ok(errorResponse);
        }

        try {
            NeoOrgWsProxy proxy = new NeoOrgWsProxy();
            OrgUserVO[] userList = proxy.searchUserByUserId(body.getUserId());
            
            if(userList == null || userList.length == 0) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "사용자를 찾을 수 없습니다.");
                return ResponseEntity.ok(errorResponse);
            }

            OrgUserVO user = userList[0];

            Map<String, String> reviewer = new HashMap<>();
            reviewer.put("userName", user.getUserName());
            reviewer.put("deptName", user.getDeptName());
            reviewer.put("userId", user.getUserId());
            reviewer.put("deptId", user.getDeptId());

            // JSON 문자열로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String reviewerJson = objectMapper.readTree(objectMapper.writeValueAsString(reviewer)).toString();

            // JSON 문자열을 저장
            gapRepository.setReviewer(Integer.parseInt(body.getGapId()), reviewerJson);

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("success", "검토자 선정 완료");
            return ResponseEntity.ok(successResponse);
        } catch (RemoteException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "사용자 조회 중 오류가 발생했습니다.");
            return ResponseEntity.ok(errorResponse);
        } catch (JsonProcessingException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "JSON 변환 중 오류가 발생했습니다.");
            return ResponseEntity.ok(errorResponse);
        }
    }   

    // 엑셀 다운로드
    public byte[] generateExcelFile(LawGapExcelRequestDto requestDto) {
        // 검색 조건 설정
        OffsetDateTime startDate = null;
        OffsetDateTime endDate = null;

        // 날짜 유효성 검사
        if ((requestDto.getStartDate() != null && requestDto.getEndDate() == null) ||
            (requestDto.getStartDate() == null && requestDto.getEndDate() != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작일 종료일 모두 입력해야 합니다.");
        }

        // 날짜 조건이 있는 경우에만 변환
        if (requestDto.getStartDate() != null && requestDto.getEndDate() != null) {
            LocalDate start = LocalDate.parse(requestDto.getStartDate(),
                    DateTimeFormatter.ofPattern("yyyyMMdd"));
            LocalDate end = LocalDate.parse(requestDto.getEndDate(),
                    DateTimeFormatter.ofPattern("yyyyMMdd"));

            startDate = start.atStartOfDay()
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toOffsetDateTime();

            endDate = end.atTime(23, 59, 59)
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toOffsetDateTime();
        }

        List<LawGapProjection> gaps = gapRepository.searchLawGapsWithoutPaging(startDate, endDate, requestDto.getKeyword(), requestDto.getFeedbackYn(), requestDto.getManagerName(), requestDto.getReviewerName());

        List<LawGapResponseDTO> result = gaps.stream()
                .map(gap -> {
                    LawGapResponseDTO dto = new LawGapResponseDTO(gap);

                    try {
                        // 분석 결과가 있는 경우 연관 사규 처리
                        if (gap.getAnalysisResult() != null && !gap.getAnalysisResult().isEmpty()) {
                            JsonNode analysisResult = new ObjectMapper().readTree(gap.getAnalysisResult());
                            
                            if(analysisResult.get("affected_company_rules") != null) {
                                String sourceString = analysisResult.get("affected_company_rules").asText();
                                
                                List<String> sources = SourceProcessor.processSourceString(sourceString);
                                List<SourceResponseDto> sourceResponseDtos = sourceProcessor.analyzePattern(sources, "INDEX_FILE_PATH");

                                if (sourceResponseDtos != null && !sourceResponseDtos.isEmpty()) {
                                    dto.setRegulations(sourceResponseDtos);
                                }
                            }
                        }
                        
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    return dto;
                })
                .collect(Collectors.toList());
        
        return _makeExcel(result, requestDto);
    }

    // 갭분석 상세 정보
    public LawGapDetailResponseDto getGapDetail(LawGapDetailRequestDto body) {
        LawGap gap = gapRepository.findByLawGapId(body.getGapId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "갭분석을 찾을 수 없습니다."));
        
        // 결과값 설정
        LawGapDetailResponseDto result = new LawGapDetailResponseDto(gap);

        // 연관 사규
        try {
            // 분석 결과가 있는 경우
            if(gap.getAnalysisResult() != null && !gap.getAnalysisResult().isEmpty()) {
                // 분석 결과 설정
                JsonNode analysisResult = new ObjectMapper().readTree(gap.getAnalysisResult());
                
                // 연관 사규가 있는 경우
                if(analysisResult.get("affected_company_rules") != null && !analysisResult.get("affected_company_rules").isNull()) {
                    String sourceString = analysisResult.get("affected_company_rules").asText();

                    // LLM 패턴에서 연관 사규 이름만 추출
                    List<String> sources = SourceProcessor.processSourceString(sourceString);
                    List<SourceResponseDto> sourceResponseDtos = sourceProcessor.analyzePattern(sources, "INDEX_FILE_PATH");

                    result.setRegulations(sourceResponseDtos);
                }

                // 제개정 이유 요약
                if(analysisResult.get("law_summary") != null && !analysisResult.get("law_summary").isNull()) {
                    result.setLawSummary(analysisResult.get("law_summary").asText());
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 피드백 확인
        Optional<LawGapFeedback> feedback = feedbackRepository.findByUserIdAndLawGap_LawGapId(body.getUserId(), gap.getLawGapId());
        result.setFeedbackYn(feedback.isPresent() ? "Y" : "N");

        // 초기 메시지 생성
        String sk = body.getUserId() + "-" + java.util.UUID.randomUUID().toString();

        Map<String, Object> aiMessage = _makeInitialChatMessage(gap, body.getUserId());

        try {
            ConversationResponseDto conversation = new ConversationResponseDto(aiMessage, sk);
            conversation.setKnowledgeIds(List.of(25, 28));
            result.setContent(List.of(conversation));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "대화 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return result;
    }

    // 갭분석 채팅 만들기
    public Map<String, String> createChat(LawGapCreateRequestDto body) {
        // gap분석 결과 갖고오기
        LawGap gap = gapRepository.findById(body.getGapId()).orElse(null);

        if (gap == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "갭분석 아이디를 찾을 수 없습니다.");
        }

        // 채팅 생성
        String SK = java.util.UUID.randomUUID().toString();
        String chatId = body.getUserId() + "-" + SK;
        String now = OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PK", new AttributeValue().withS(body.getUserId()));
        item.put("SK", new AttributeValue().withS(chatId));
        item.put("create_time", new AttributeValue().withS(now));
        item.put("update_time", new AttributeValue().withS(now));
        item.put("llm_model", new AttributeValue().withS("bedrock"));
        item.put("title", new AttributeValue().withS(gap.getTitle() + "분석 결과"));
        item.put("gap_type", new AttributeValue().withBOOL(true));

        // message 생성
        Map<String, AttributeValue> message = new HashMap<>();

        // 분석 결과 설정
        String answer = "";
        List<String> sources = new ArrayList<>();
        if(gap.getAnalysisResult() != null && !gap.getAnalysisResult().isEmpty()) {
            try {
                JsonNode analysisResult = new ObjectMapper().readTree(gap.getAnalysisResult());
                
                JsonNode gapAnalysisSummary = analysisResult.get("gap_analysis_summary");

                answer = gapAnalysisSummary != null ? gapAnalysisSummary.asText() : "";

                sources = Arrays.asList(analysisResult.get("affected_company_rules").asText().split("\n"));
                
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        String messageId = "message-" + java.util.UUID.randomUUID().toString();
        message.put("answer", new AttributeValue().withS(answer));
        message.put("category", new AttributeValue().withL(Arrays.asList(
            new AttributeValue().withS("law"),
            new AttributeValue().withS("i-portal")
        )));
        message.put("message_id", new AttributeValue().withS(messageId));
        message.put("qa_create_time", new AttributeValue().withS(now));
        message.put("query", new AttributeValue().withS(gap.getTitle() + "분석 결과"));
        message.put("gap_type", new AttributeValue().withBOOL(true));

        message.put("source", new AttributeValue().withL(
            sources.stream()
                .map(s -> new AttributeValue().withS(s + " ['i-portal']"))
                .collect(Collectors.toList())
        ));

        // 메시지 설정
        AttributeValue messages = new AttributeValue().withL(Arrays.asList(
            new AttributeValue().withM(message)
        ));

        item.put("message", messages);
        chatHistoryRepository.save(item);

        Map<String, String> result = new HashMap<>();
        result.put("chatId", chatId);
        result.put("conversationId", messageId);
        return result;
    }

    // 갭분석 피드백
    public Boolean feedbackGap(LawGapFeedbackRequestDto body) {
        LawGap gap = gapRepository.findById(body.getGapId()).orElse(null);
        if (gap == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "갭분석 아이디를 찾을 수 없습니다.");
        }

        // 피드백 중복 검사
        Optional<LawGapFeedback> feedback = feedbackRepository.findByUserIdAndLawGap_LawGapId(body.getUserId(), gap.getLawGapId());

        if(feedback.isPresent()) {
            // 피드백 업데이트
            feedback.get().setFeedback(Optional.ofNullable(body.getFeedback()).filter(s -> !s.isEmpty()).orElse(null));
            feedback.get().setComment(Optional.ofNullable(body.getComment()).filter(s -> !s.isEmpty()).orElse(null));
            
            feedbackRepository.save(feedback.get());
        } else {
            LawGapFeedback newFeedback = new LawGapFeedback();
            newFeedback.setUserId(body.getUserId());
            newFeedback.setFeedback(Optional.ofNullable(body.getFeedback()).filter(s -> !s.isEmpty()).orElse(null));
            newFeedback.setComment(Optional.ofNullable(body.getComment()).filter(s -> !s.isEmpty()).orElse(null));
            newFeedback.setLawGap(gap);
            feedbackRepository.save(newFeedback);
        }

        return true;
    }

    @Transactional
    public Boolean deleteFeedback(LawGapFeedbackRequestDto body) {
        feedbackRepository.deleteByUserIdAndLawGap_LawGapId(body.getUserId(), body.getGapId());
        return true;
    }

    // 엑셀 파일 생성
    private byte[] _makeExcel(List<LawGapResponseDTO> gaps, LawGapExcelRequestDto requestDto) {
        int currentRowNum = 0;

        NeoOrgWsProxy proxy = AI_NEOSLO_ENABLED ? new NeoOrgWsProxy() : null;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("갭분석 목록");
            
            // 셀 스타일 생성 - 줄바꿈 설정
            CellStyle wrapStyle = workbook.createCellStyle();
            wrapStyle.setWrapText(true);

            // requestDto 값 검증
            if (requestDto.getStartDate() != null || requestDto.getEndDate() != null || requestDto.getKeyword() != null || requestDto.getFeedbackYn() != null) {
                List<String> conditions = new ArrayList<>();
                
                if (requestDto.getStartDate() != null) {
                    conditions.add("시작일: " + requestDto.getStartDate());
                }
                if (requestDto.getEndDate() != null) {
                    conditions.add("종료일: " + requestDto.getEndDate());
                }
                if (requestDto.getKeyword() != null) {
                    conditions.add("키워드: " + requestDto.getKeyword());
                }

                if (requestDto.getFeedbackYn() != null) {
                    conditions.add("피드백 여부: " + requestDto.getFeedbackYn());
                }

                Row conditionRow = sheet.createRow(currentRowNum++);
                Cell conditionCell = conditionRow.createCell(0);
                conditionCell.setCellValue("검색조건  " + String.join(" ", conditions));
                conditionRow.setHeight((short)-1); // 행 높이 자동 조정
            }

            // 헤더 생성
            Row headerRow = sheet.createRow(currentRowNum++);
            String[] headers = {"ID", "법령명", "제개정일", "시행일", "변경내용", "연관사규", "분석결과", "피드백 여부", "담당자", "피드백 내용"};
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
            headerStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(headers[i]);
            }

            // 데이터 행 생성
            for (LawGapResponseDTO gap : gaps) {
                Row row = sheet.createRow(currentRowNum++);
                
                // 각 셀에 줄바꿈 스타일 적용
                Cell[] cells = new Cell[headers.length];
                for (int i = 0; i < cells.length; i++) {
                    cells[i] = row.createCell(i);
                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setWrapText(true); // 줄바꿈 설정
                    cellStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.TOP); // 상단 정렬
                    
                    if (i == 0) {
                        cellStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT); // 우측 정렬
                    } else {
                        cellStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT); // 좌측 정렬
                    }
                    
                    cells[i].setCellStyle(cellStyle);
                }
                
                
                cells[0].setCellValue(gap.getLawGapId());
                cells[1].setCellValue(gap.getTitle());
                cells[2].setCellValue(gap.getRegDt());
                cells[3].setCellValue(String.join(", ", gap.getEffDts()));
                cells[4].setCellValue(gap.getAmendment());
                cells[5].setCellValue(gap.getRegulations() != null ? 
                    gap.getRegulations().stream()
                        .map(SourceResponseDto::getTitle)
                        .collect(Collectors.joining("\n")) : 
                    "");
                cells[6].setCellValue(gap.getAnalysisResult());
                cells[7].setCellValue(gap.getFeedback());
                if(gap.getReviewer() != null) {
                    cells[8].setCellValue(gap.getReviewer().getUserName() + " (" + gap.getReviewer().getDeptName() + ")");
                }

                // 피드백 내용 확인
                List<LawGapFeedback> feedbacks = feedbackRepository.findByLawGap_LawGapId(gap.getLawGapId().intValue());
                if(!feedbacks.isEmpty()) {
                    List<String> feedbackContents = new ArrayList<>();
                    for (LawGapFeedback feedback : feedbacks) {
                        OrgUserVO userInfo = null;
                        if(proxy != null) {
                            OrgUserVO[] userList = proxy.searchUserByUserId(feedback.getUserId());
                            if(userList != null && userList.length > 0) {
                                userInfo = userList[0];
                            }
                        }

                        // 등록 날짜
                        String feedbackDate = feedback.getCreatedAt()
                            .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        // 유저 정보
                        if(userInfo != null) {
                            feedbackContents.add(userInfo.getUserName() + " (" + userInfo.getDeptName() + ")" + " - " + feedbackDate + "\n" + feedback.getComment());
                        } else {
                            feedbackContents.add(feedback.getUserId() + " - " + feedbackDate + "\n" + feedback.getComment());
                        }
                    }

                    cells[9].setCellValue(String.join("\n\n", feedbackContents));
                }

                // 행 높이 자동 조정
                row.setHeight((short)-1);
            }

            // 열 너비 자동 조정
            for (int i = 1; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 워크북을 바이트 배열로 변환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 생성 중 오류가 발생했습니다.", e);
        }
    }

    // 초기 메시지 생성
    private Map<String, Object> _makeInitialChatMessage(LawGap gap, String userId) {
        Map<String, Object> aiMessage = new HashMap<>();
        String messageId = "message-" + java.util.UUID.randomUUID().toString(); 
        String now = OffsetDateTime.now()
                .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        aiMessage.put("message_id", messageId);
        aiMessage.put("qa_create_time", now);
        aiMessage.put("query", "");
        aiMessage.put("sources", new ArrayList<>());
        aiMessage.put("knowledge", new ArrayList<>());
        aiMessage.put("content", new ArrayList<>());

        // 분석 결과 처리
        if (gap.getAnalysisResult() != null && !gap.getAnalysisResult().isEmpty()) {
            try {
                JsonNode analysisResult = new ObjectMapper().readTree(gap.getAnalysisResult());
                
                // 답변 설정
                String answer = analysisResult.get("gap_analysis_summary").asText();
                aiMessage.put("answer", answer);

                JsonNode affectedRules = analysisResult.get("affected_company_rules");
                
                List<String> chatSources = SourceProcessor.processSourceString(affectedRules.asText());

                List<SourceResponseDto> sourceResponseDtos = sourceProcessor.analyzePattern(chatSources, "INDEX_FILE_PATH");

                aiMessage.put("sources", sourceResponseDtos);

                // knowledge 처리
                List<Knowledge> knowledges = knowledgeRepository.findAllByKnowledgeIdIn(List.of(25, 28));

                if (!knowledges.isEmpty()) {
                    aiMessage.put("knowledge", knowledges);
                }
                
            } catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 파싱 중 오류가 발생했습니다.", e);
            }
        } else {
            aiMessage.put("answer", "분석 결과가 없습니다.");
        }

        return aiMessage;
    }

    // 피드백 목록 갖고오기
    public List<LawGapFeedbackResponseDto> getFeedbackList(FeedbackListRequestDto body) {
        List<LawGapFeedback> feedbacks = feedbackRepository.findByLawGap_LawGapId(Integer.parseInt(body.getLawGapId()));

        final NeoOrgWsProxy proxy = AI_NEOSLO_ENABLED ? new NeoOrgWsProxy() : null;

        List<LawGapFeedbackResponseDto> result = feedbacks.stream()
            .map(feedback -> {
                LawGapFeedbackResponseDto dto = new LawGapFeedbackResponseDto(feedback);

                if(proxy != null) {
                    try {
                        OrgUserVO[] userList = proxy.searchUserByUserId(feedback.getUserId());    

                        if(userList != null && userList.length > 0) {
                            OrgUserVO user = userList[0];
                            dto.setDeptName(user.getDeptName());
                            dto.setUserName(user.getUserName());
                            dto.setUserId(user.getUserId());
                            dto.setDeptId(user.getDeptId());
                        }

                    } catch (RemoteException e) {
                        System.err.println("사용자 조회 중 오류 발생: " + e.getMessage());
                    }   
                }
                return dto;
            })
            .collect(Collectors.toList());

        return result;
    }
}
