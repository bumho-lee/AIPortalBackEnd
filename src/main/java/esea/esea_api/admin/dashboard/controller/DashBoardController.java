package esea.esea_api.admin.dashboard.controller;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.YearMonth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.admin.dashboard.dto.DashBoardChatDto;
import esea.esea_api.admin.dashboard.dto.DashBoardTokenDto;
import esea.esea_api.admin.dashboard.dto.DashBoardTransDto;
import esea.esea_api.admin.dashboard.dto.DashBoardUserDto;
import esea.esea_api.admin.dashboard.service.DashBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DASHBOARD API", description = "DASHBOARD API")
@RestController
@RequestMapping("/dashBoard")
public class DashBoardController {

	private final DashBoardService dashBoardService;
	
	public DashBoardController(DashBoardService dashBoardService) {
        this.dashBoardService = dashBoardService;
    }
	
	@Operation(summary = "DASHBOARD 조회", description = "DASHBOARD 조회")
    @GetMapping("/search")
    public ResponseEntity<?> dashBoardSearch(
    		@RequestParam(value = "type") String type,
    		@RequestParam(value = "sMonth",required = false) String sMonth,
    		@RequestParam(value = "eMonth",required = false) String eMonth) {

		Map<String, Object> response = new HashMap<>();
		List<DashBoardUserDto> dashBoardUserCount;
		List<DashBoardChatDto> dashBoardChatCount;

		String startDate = sMonth;
		String endDate = eMonth;

		// 주간
		if (type.equals("W")){
			dashBoardUserCount = dashBoardService.dashBoardUserWeekCount(startDate, endDate);
        	dashBoardChatCount = dashBoardService.dashBoardChatWeekCount(startDate, endDate);
		// 연간
		} else {
			// 날짜 변환
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMM");
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			// 시작 날짜
			YearMonth startYearMonth = YearMonth.parse(startDate, inputFormatter);
			YearMonth endYearMonth = YearMonth.parse(endDate, inputFormatter); 

			// 날짜 추출
			startDate = startYearMonth.atDay(1).format(outputFormatter);
			endDate = endYearMonth.atEndOfMonth().format(outputFormatter);

			dashBoardUserCount = dashBoardService.dashBoardUserMonthCount(startDate, endDate);
        	dashBoardChatCount = dashBoardService.dashBoardChatMonthCount(startDate, endDate);
		}
        
        response.put("userCount", dashBoardUserCount);
        response.put("chatCount", dashBoardChatCount);
        
        return ResponseEntity.ok(response);
    }
	
	@Operation(summary = "DASHBOARD사용량 조회", description = "DASHBOARD 조회")
    @GetMapping("/usage")
    public ResponseEntity<?> dashBoardToken(
    		@RequestParam(value = "llmModel") String llmModel,
    		@RequestParam(value = "sMonth",required = false) String sMonth,
    		@RequestParam(value = "eMonth",required = false) String eMonth) {
		Map<String, Object> response = new HashMap<>();

		// 날짜 변환
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMM");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// 시작 날짜
		YearMonth startYearMonth = YearMonth.parse(sMonth, inputFormatter);
		YearMonth endYearMonth = YearMonth.parse(eMonth, inputFormatter); 

		// 날짜 추출
		String startDate = startYearMonth.atDay(1).format(outputFormatter);
		String endDate = endYearMonth.atEndOfMonth().format(outputFormatter);

		// 날짜 변환
		List<DashBoardTokenDto>	dashBoardTokenCount = dashBoardService.dashBoardTokenCount(llmModel, startDate, endDate);
		List<DashBoardTransDto>	dashBoardTransCount = dashBoardService.findMonthlyTranslationSummary(sMonth, eMonth);
        
        response.put("tokenCount", dashBoardTokenCount);
        response.put("transCount", dashBoardTransCount);
        
        return ResponseEntity.ok(response);
    }
}
