package esea.esea_api.statistics;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import esea.esea_api.repositories.DailyLlmUserUsageRepository;
import esea.esea_api.repositories.ConversationLogRepository;
import esea.esea_api.repositories.ConversationReactionRepository;
import esea.esea_api.repositories.KnowledgeRepository;

import esea.esea_api.statistics.dto.ConversationLogRequestDto;
import esea.esea_api.statistics.dto.ConversationLogResponseDto;
import esea.esea_api.statistics.dto.DailyLlmUserUsageDto;
import esea.esea_api.statistics.dto.UserLlmUsageRequestDto;
import esea.esea_api.statistics.dto.ConversationLogDto;
import esea.esea_api.statistics.dto.DailyLlmUserUsageDtoImpl;
import esea.esea_api.statistics.dto.UserLlmUsageExcelRequestDto;
import esea.esea_api.statistics.dto.ConversationLogExcelRequestDto;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import esea.esea_api.entities.ConversationReaction;
import esea.esea_api.entities.Knowledge;
import esea.esea_api.admin.llm.entities.LlmEntService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.data.domain.Page;

import esea.esea_api.statistics.dto.UserLlmUsageResponseDto;
import esea.org.service.NeoOrgWsProxy;
import esea.org.vo.OrgUserVO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

import esea.esea_api.admin.llm.repository.LlmRepository;
import esea.esea_api.entities.ConversationLog;

@Service
public class StatisticsService {
    private final Environment env;
    private final boolean AI_NEOSLO_ENABLED;
    
    public StatisticsService(Environment env) {
        this.env = env;
        this.AI_NEOSLO_ENABLED = Boolean.parseBoolean(env.getProperty("neoslo.enabled", "true"));
    }

    @Autowired 
    private DailyLlmUserUsageRepository dailyLlmUserUsageRepository;

    @Autowired
    private ConversationLogRepository conversationLogRepository;

    @Autowired
    private ConversationReactionRepository conversationReactionRepository;

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    @Autowired
    private LlmRepository llmRepository;

    public UserLlmUsageResponseDto getUserUsage(UserLlmUsageRequestDto requestDto) {
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


        Page<DailyLlmUserUsageDto> usagePage = dailyLlmUserUsageRepository.searchUserUsageGroupByUser(startDate, endDate, requestDto.getUserId(), requestDto.getDeptId(), requestDto.getModel(), pageRequest);

        UserLlmUsageResponseDto response = new UserLlmUsageResponseDto();

        // userId로 userName 조회하여 설정
        List<DailyLlmUserUsageDto> content = new ArrayList<>();

        // 사용자 이름 조회
        final NeoOrgWsProxy proxy = AI_NEOSLO_ENABLED ? new NeoOrgWsProxy() : null;
        usagePage.getContent().forEach(item -> {
            DailyLlmUserUsageDtoImpl modifiableItem = new DailyLlmUserUsageDtoImpl(item);
            
            if(proxy != null) {
                try {
                    String userId = item.getUserId();
                    OrgUserVO[] userData = proxy.searchUserByUserId(userId);
                    modifiableItem.setUserName(userData[0].getUserName());
                    modifiableItem.setDeptNm(userData[0].getDeptName());
                } catch (RemoteException e) {
                    // 에러 로그 추가
                    System.err.println("사용자 이름 조회 실패: " + item.getUserId());
                } catch (Exception e) {
                    // 일반 예외 처리
                    System.err.println("예기치 않은 오류 발생: " + e.getMessage());
                }
            }

            content.add(modifiableItem);
        });
        
        response.setContent(content);
        response.setTotalCount((int) usagePage.getTotalElements());

        return response;
    }

    // 엑셀 다운로드
    public byte[] downloadUserUsageExcel(UserLlmUsageExcelRequestDto requestDto) {
        // 엑셀 다운로드 로직 추가
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

        List<DailyLlmUserUsageDto> items = dailyLlmUserUsageRepository.searchUserUsageGroupByUserList(startDate, endDate, requestDto.getUserId(), requestDto.getDeptId(), requestDto.getModel());

        // 사용자 이름 조회
        List<DailyLlmUserUsageDto> raws = new ArrayList<>();
        final NeoOrgWsProxy proxy = AI_NEOSLO_ENABLED ? new NeoOrgWsProxy() : null;
        items.forEach(item -> {
            DailyLlmUserUsageDtoImpl modifiableItem = new DailyLlmUserUsageDtoImpl(item);
            
            if(proxy != null) {
                try {
                    String userId = item.getUserId();
                    OrgUserVO[] userData = proxy.searchUserByUserId(userId);
                    modifiableItem.setUserName(userData[0].getUserName());
                    modifiableItem.setDeptNm(userData[0].getDeptName());
                } catch (RemoteException e) {
                    // 에러 로그 추가
                    System.err.println("사용자 이름 조회 실패: " + item.getUserId());
                } catch (Exception e) {
                    // 일반 예외 처리
                    System.err.println("예기치 않은 오류 발생: " + e.getMessage());
                }
            }

            raws.add(modifiableItem);
        });

        return _makeLLmUsageExcel(raws, requestDto);
    }

    // 대화 내역 조회
    public ConversationLogResponseDto getUserConversationLog(ConversationLogRequestDto requestDto) {
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

        Page<ConversationLog> conversationLogPage = conversationLogRepository.searchConversationLogs(startDate, endDate, requestDto.getUserId(), requestDto.getModel(), requestDto.getLikeDislike(), pageRequest);

        // dto 변환
        final NeoOrgWsProxy proxy = AI_NEOSLO_ENABLED ? new NeoOrgWsProxy() : null;
        List<ConversationLogDto> content = conversationLogPage.getContent().stream()
            .map(log -> {
                ConversationLogDto dto = new ConversationLogDto(log);

                // 사용자 이름 조회
                if(proxy != null) {
                    try {
                        OrgUserVO[] userData = proxy.searchUserByUserId(dto.getUserId());
                        dto.setUserName(userData[0].getUserName());
                    } catch (RemoteException e) {
                        System.err.println("사용자 이름 조회 실패: " + dto.getUserId());
                    } catch (Exception e) {
                        System.err.println("예기치 않은 오류 발생: " + e.getMessage());
                    }
                }

                // 대화 평가 조회
                ConversationReaction reaction = conversationReactionRepository.findByConversationId(log.getConversationId());

                if (reaction != null) {
                    dto.setLikeDislike(reaction.getType().toString());
                    dto.setReason(reaction.getReason());
                }

                // 선택한 지식 체계 조회
                List<Knowledge> knowledges = new ArrayList<>();
                for (String indexCode : log.getCategory()) {
                    List<Knowledge> knowledgeRaws = knowledgeRepository.findAllByIndexCode(indexCode);

                    if(!knowledgeRaws.isEmpty()) {
                        knowledges.addAll(knowledgeRaws);
                    }
                }

                dto.setKnowledgeIds(knowledges.stream().map(Knowledge::getKnowledgeId).collect(Collectors.toList()));

                return dto;
            })
            .collect(Collectors.toList());

        ConversationLogResponseDto response = new ConversationLogResponseDto();
        response.setContent(content);
        response.setTotalCount((int) conversationLogPage.getTotalElements());

        return response;
    }

    // 대화 로그 엑셀 다운로드
    public byte[] downloadUserConversationLogExcel(ConversationLogExcelRequestDto requestDto) {
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

        List<ConversationLog> items = conversationLogRepository.searchConversationLogsList(startDate, endDate, requestDto.getUserId(), requestDto.getModel(), requestDto.getLikeDislike());

        // dto 변환
        final NeoOrgWsProxy proxy = AI_NEOSLO_ENABLED ? new NeoOrgWsProxy() : null;
        List<ConversationLogDto> rowDatas = items.stream()
            .map(log -> {
                ConversationLogDto dto = new ConversationLogDto(log);

                // 사용자 이름 조회
                if(proxy != null) {
                    try {
                        OrgUserVO[] userData = proxy.searchUserByUserId(dto.getUserId());
                        dto.setUserName(userData[0].getUserName());
                    } catch (RemoteException e) {
                        System.err.println("사용자 이름 조회 실패: " + dto.getUserId());
                    } catch (Exception e) {
                        System.err.println("예기치 않은 오류 발생: " + e.getMessage());
                    }
                }

                // 대화 평가 조회
                ConversationReaction reaction = conversationReactionRepository.findByConversationId(log.getConversationId());

                if (reaction != null) {
                    dto.setLikeDislike(reaction.getType().toString());
                    dto.setReason(reaction.getReason());
                }

                // 선택한 지식 체계 조회
                List<Knowledge> knowledges = new ArrayList<>();
                for (String indexCode : log.getCategory()) {
                    List<Knowledge> knowledgeRaws = knowledgeRepository.findAllByIndexCode(indexCode);

                    if(!knowledgeRaws.isEmpty()) {
                        knowledges.addAll(knowledgeRaws);
                    }
                }

                dto.setKnowledgeNames(knowledges.stream().map(Knowledge::getDisplayName).collect(Collectors.toList()));

                return dto;
            })
            .collect(Collectors.toList());

        return _makeConversationLogExcel(rowDatas, requestDto);
    }

    // 사용량 엑셀 다운로드
    private byte[] _makeLLmUsageExcel(List<DailyLlmUserUsageDto> raws, UserLlmUsageExcelRequestDto requestDto) {
        LlmEntService llmService = null;
        int currentRowNum = 0;
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("유저별 LLM 사용량 목록");
            
            // 셀 스타일 생성 - 줄바꿈 설정
            CellStyle wrapStyle = workbook.createCellStyle();
            wrapStyle.setWrapText(true);

            // requestDto 값 검증
            if (requestDto.getStartDate() != null || requestDto.getEndDate() != null || requestDto.getModel() != null || requestDto.getUserId() != null) {
                List<String> conditions = new ArrayList<>();
                
                if (requestDto.getStartDate() != null) {
                    conditions.add("시작일: " + requestDto.getStartDate());
                }
                if (requestDto.getEndDate() != null) {
                    conditions.add("종료일: " + requestDto.getEndDate());
                }
                if (requestDto.getModel() != null) {
                    llmService = llmRepository.findByServiceKey(requestDto.getModel());
                    conditions.add("모델: " + llmService.getName());
                }

                if (requestDto.getUserId() != null) {
                    conditions.add("사용자: " + requestDto.getUserId());
                }
                
                Row conditionRow = sheet.createRow(currentRowNum++);
                Cell conditionCell = conditionRow.createCell(0);
                conditionCell.setCellValue("검색조건  " + String.join(" ", conditions));
                conditionRow.setHeight((short)-1); // 행 높이 자동 조정
            }

            // 헤더 생성
            Row headerRow = sheet.createRow(currentRowNum++);
            String[] headers = {"NO", "대화 횟수", "부서/부문", "이름", "ID", "LLM", "입력 Token", "출력 Token", "입/출력 합계 Token"};
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
            headerStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(headers[i]);
            }

            // 데이터 행 생성
            for (DailyLlmUserUsageDto raw : raws) {
                Row row = sheet.createRow(currentRowNum++);
                
                if(llmService == null) {
                    llmService = llmRepository.findByServiceKey(raw.getLlmModel());
                }

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

                // 숫자 포맷 설정
                CellStyle numberStyle = workbook.createCellStyle();
                numberStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
                cells[6].setCellStyle(numberStyle);
                cells[7].setCellStyle(numberStyle); 
                cells[8].setCellStyle(numberStyle);

                cells[0].setCellValue(currentRowNum);
                cells[1].setCellValue(raw.getCount());
                cells[2].setCellValue(raw.getDeptNm());
                cells[3].setCellValue(raw.getUserName());
                cells[4].setCellValue(raw.getUserId());
                cells[5].setCellValue(llmService.getName());
                cells[6].setCellValue(raw.getInputUsage());
                cells[7].setCellValue(raw.getOutputUsage());
                cells[8].setCellValue(raw.getInputUsage() + raw.getOutputUsage());

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

    // 대화 로그 엑셀 다운로드
    private byte[] _makeConversationLogExcel(List<ConversationLogDto> rowDatas, ConversationLogExcelRequestDto requestDto) {
        LlmEntService llmService = null;
        int currentRowNum = 0;
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("유저별 대화 로그 목록");
            
            // 셀 스타일 생성 - 줄바꿈 설정
            CellStyle wrapStyle = workbook.createCellStyle();
            wrapStyle.setWrapText(true);

            // requestDto 값 검증
            if (requestDto.getStartDate() != null || requestDto.getEndDate() != null || requestDto.getModel() != null || requestDto.getUserId() != null) {
                List<String> conditions = new ArrayList<>();
                
                if (requestDto.getStartDate() != null) {
                    conditions.add("시작일: " + requestDto.getStartDate());
                }
                if (requestDto.getEndDate() != null) {
                    conditions.add("종료일: " + requestDto.getEndDate());
                }
                if (requestDto.getUserId() != null) {
                    conditions.add("사용자: " + requestDto.getUserId());
                }
                if (requestDto.getModel() != null) {
                    llmService = llmRepository.findByServiceKey(requestDto.getModel());
                    conditions.add("모델: " + llmService.getName());
                }
                if (requestDto.getLikeDislike() != null) {
                    conditions.add("평가: " + requestDto.getLikeDislike());
                }
                
                Row conditionRow = sheet.createRow(currentRowNum++);
                Cell conditionCell = conditionRow.createCell(0);
                conditionCell.setCellValue("검색조건  " + String.join(" ", conditions));
                conditionRow.setHeight((short)-1); // 행 높이 자동 조정
                
            }

            // 헤더 생성
            Row headerRow = sheet.createRow(currentRowNum++);
            String[] headers = {"NO", "질문", "답변", "LLM", "지식", "ID", "이름", "평가", "DisLike 내용", "일시"};
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
            headerStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.TOP);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(headers[i]);
            }

            // 데이터 행 생성
            for (ConversationLogDto rowData : rowDatas) {
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

                if(llmService == null) {
                    llmService = llmRepository.findByServiceKey(rowData.getModel());
                }

                // 숫자 포맷 설정
                cells[0].setCellValue(currentRowNum);
                cells[1].setCellValue(rowData.getQuery());
                cells[2].setCellValue(rowData.getAnswer());
                cells[3].setCellValue(llmService.getName());
                cells[4].setCellValue(String.join(", ", rowData.getKnowledgeNames()));
                cells[5].setCellValue(rowData.getUserId());
                cells[6].setCellValue(rowData.getUserName());
                cells[7].setCellValue(rowData.getLikeDislike());
                cells[8].setCellValue(rowData.getReason());
                cells[9].setCellValue(rowData.getRegDt());

                // 행 높이 자동 조정
                row.setHeight((short)-1);
            }

            // 열 너비 수동 및 자동 설정
            sheet.setColumnWidth(0, 500);  // NO
            sheet.setColumnWidth(1, 15000);  // 질문
            sheet.setColumnWidth(2, 15000);  // 답변

            // 나머지 열은 자동 너비 설정
            for (int i = 0; i < headers.length; i++) {
                if (i != 1 && i != 2) {  // 질문과 답변 열은 이미 설정됨
                    sheet.autoSizeColumn(i);
                }
            }

            // 워크북을 바이트 배열로 변환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일 생성 중 오류가 발생했습니다.", e);
        }
    }
}
