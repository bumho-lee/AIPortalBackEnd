package esea.esea_api.admin.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.admin.login.dto.LoginLogRequest;
import esea.esea_api.admin.login.dto.LoginLogSearchDTO;
import esea.esea_api.admin.login.entities.UserLoginHistory;
import esea.esea_api.admin.login.service.LoginLogService;
import esea.esea_api.translation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Tag(name = "로그인 API", description = "로그인 조회 API")
@RestController
@RequestMapping("/login")
public class LoginLogController {
	
	private final LoginLogService loginLogService;
	
    @Autowired
    public LoginLogController(LoginLogService loginLogService) {
        this.loginLogService = loginLogService;
    }
    
    @Operation(
            summary = "날짜별/등록자별 로그인로그 조회",
            description = "특정 날짜 범위와 이름을 로그인로그를 페이징하여 조회합니다."
        )
        @PostMapping("/search")
        public ResponseEntity<PageResponse<UserLoginHistory>> getFilesByDateRangeAndRegId(
                @RequestBody(required = false) LoginLogSearchDTO searchDTO) {
            
            if (searchDTO == null) {
                searchDTO = new LoginLogSearchDTO();
            }

            Page<UserLoginHistory> result = loginLogService.getLoginHistory(
                searchDTO.getStartDate(),
                searchDTO.getEndDate(),
                searchDTO.getUserNm(),
                searchDTO.getDeptNm(),
                searchDTO.getPage(),
                searchDTO.getSize()
            );

            // loginTime을 포맷팅된 문자열로 변환
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
            result.getContent().forEach(history -> {
                LocalDateTime loginTime = history.getLoginTime();
                
                if (loginTime != null) { // null 체크 추가
                    ZonedDateTime koreaTime = loginTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(koreaZoneId);
                    String formattedLoginTime = koreaTime.format(dateTimeFormatter);
                    history.setFormattedLoginTime(formattedLoginTime); // 새로운 필드로 설정
                }
            });

            return ResponseEntity.ok(PageResponse.of(result));
    }
    
    /**
     * 로그인 이력 저장
     */
    @Operation(
        summary = "로그인로그 이력저장",
        description = "로그인로그 이력저장합니다."
    )
    @PostMapping("/save")
    public ResponseEntity<?> saveLoginHistory(
            @RequestParam("userId") String userId,
            @RequestParam("userNm") String userNm,
            @RequestParam("ipAddr") String ipAddr) {
        try {
            
        	LoginLogRequest reqLoginInfo = new LoginLogRequest();
            reqLoginInfo.setUserId(userId);
            reqLoginInfo.setUserNm(userNm);
            reqLoginInfo.setIpAddr(ipAddr);
            
            UserLoginHistory savedLog = loginLogService.saveLoginHistory(reqLoginInfo);
            return ResponseEntity.ok().body(savedLog);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("로그인 이력 저장 중 오류가 발생했습니다.");
        }
    }
}
