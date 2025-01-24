package esea.esea_api.slo;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.*;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// slo 서비스 클라이언트
import esea.backend.neoslo.NeoSloClient;
import esea.esea_api.admin.login.dto.LoginLogRequest;
import esea.esea_api.admin.login.entities.UserLoginHistory;
import esea.esea_api.admin.login.service.LoginLogService;
import esea.esea_api.admin.menu.entities.MenuRole;
import esea.esea_api.admin.menu.service.MenuRoleService;
import esea.esea_api.admin.role.entities.RoleManager;
import esea.esea_api.admin.role.service.RoleManagerService;
import esea.esea_api.common.ClientIPFinder;
import esea.esea_api.slo.dto.SloDto;
import esea.esea_api.translation.entities.TranslationManager;
import esea.esea_api.translation.service.TranslationManagerService;
import esea.org.service.NeoOrgWsProxy;
import esea.org.vo.OrgDeptVO;
import esea.org.vo.OrgUserVO;
import esea.prodSlo.service.NeoProdSloClient;
import hanwha.neo.slo.SLODecrypt4AES;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Log4j2
@Tag(name = "SLO", description = "SLO")
@RestController
@RequestMapping("/slo")
public class SloController {
    @Autowired(required = false)
    private NeoSloClient neoSloClient;

    @Autowired(required = false)
    private NeoProdSloClient neoProdSloClient;
    
    @Autowired
	private LoginLogService loginLogService;
	
	@Autowired
	private RoleManagerService roleManagerService;
	
	@Autowired
	private MenuRoleService menuRoleService;
	
    @Autowired
    private TranslationManagerService translationManagerService;
	    
    
    @PostMapping(value = "/sloKey", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> sloKey(
            @RequestParam Map<String, String> requestParams) {
    	Map<String, String> response = new HashMap<>();
    	
    	if (neoSloClient == null) {
            response.put("message", "Neoslo service is currently disabled");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }
    	
    	String sloKey = requestParams.get("slo_p_ota");
		log.info("현재 sloKey: {}", sloKey);
    	
		String sloRedirectUrl = StringUtils.defaultIfEmpty(System.getenv("SLO_REDIRECT_URL"), "https://ai.hanwha-total.net/slo/authPage?sloKey=%s");
		//sloRedirectUrl = "http://localhost:5173/slo/authPage?sloKey=%s";
		log.info("현재 sloRedirectUrl: {}", sloRedirectUrl);

    	// sloKey를 쿼리 파라미터로 포함한 URL 생성
    	String redirectUrl = String.format(
    	    sloRedirectUrl,
			URLEncoder.encode(sloKey, StandardCharsets.UTF_8) // URL 인코딩
    	);
		log.info("현재 redirectUrl: {}", redirectUrl);
        
        // HttpHeaders에 Location 헤더 추가
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).build();
    	
    }
    
    @Operation(summary = "slo 인증 시도", description = "slo 인증 시도")
    @PostMapping("/sloAuth")
    public ResponseEntity<?> sloAuth(
    		@RequestBody SloDto body, HttpServletRequest request) throws RemoteException {
    	Map<String, Object> response = new HashMap<>();
    	Map<String, Object> errorResponse = new HashMap<>();
    	
    	// if (neoSloClient == null) {
        //     response.put("message", "Neoslo service is currently disabled");
        //     return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        // }

		String SERVER_ENV = StringUtils.defaultIfEmpty(System.getenv("SERVER_ENV"), "prod");
    	String WS_TARGET = StringUtils.defaultIfEmpty(System.getenv("SLO_WS_TARGET"), "AI_PORTAL");
    	String SEED = StringUtils.defaultIfEmpty(System.getenv("SLO_SEED"), "1556889699646683");
        log.info("slokey : {} / {} / {}", body.getSloKey(), WS_TARGET, SEED);
    	
		String encUserInfo = null;
		// 환경 분리
		if(SERVER_ENV.equals("prod")) {
			log.info("test11");
			encUserInfo = neoProdSloClient.login(body.getSloKey(), WS_TARGET);
		} else {
			log.info("test22");
			encUserInfo = neoSloClient.login(body.getSloKey(), WS_TARGET);
		}

		  String userInfo = SLODecrypt4AES.decrypt(encUserInfo, SEED);
		  String text = new String(userInfo.getBytes(), StandardCharsets.UTF_8);
        log.info("result : {}", text);

        log.info("encUserInfo : {}", encUserInfo);
		  //세미콜론을 기준으로 문자열 분리 
		  String[] result = text.split(";");
		  
		  // 개별 필드 접근 예시 
		  String userId = result[0]; // ID 

	    //   final String END_POINT = "http://htgdev.circle.hanwha.com/api/ws/org?type=503";
		try {
			NeoOrgWsProxy proxy = new NeoOrgWsProxy();
			OrgDeptVO[] deptAllList = null;
			// 사용자 데이터 처리
			OrgUserVO[] userData = proxy.searchUserByUserId(userId);
			log.info("userData : {}", userData[0].toString());

			List<TranslationManager> translationManagerList = translationManagerService.getAllTranslationManagers();

			String userDeptId = userData[0].getDeptId();
			log.info("userDeptId : {}", userDeptId);

			// TranslationManager 리스트 순회
			for (TranslationManager translationManager : translationManagerList) {
				// TranslationManager에서 부서 ID 가져오기
				String departmentId = translationManager.getDepartment_id();
				log.info("departmentId : {}", departmentId);
				// 부서 ID에 해당하는 부서 정보 조회 (OrgDeptVO[] 배열)
				deptAllList = proxy.searchDeptByDeptIdIncludeChild(departmentId);
				
				// deptAllList 배열을 순회하면서 userDeptId와 비교
				boolean deptFound = false;  // 부서 ID 일치 여부를 추적하는 변수

				for (OrgDeptVO dept : deptAllList) {
					log.info("dept : {}", dept.toString());
					if (dept.getDeptId().equals(userDeptId)) {
						deptFound = true;  // 부서 ID가 일치하는 경우
						break;  // 일치하는 부서를 찾았으므로 더 이상 순회할 필요 없음
					}
				}
				log.info("deptFound : {}", deptFound);
				if (deptFound) {
					break;
				}else {
					log.info("등록된 사용자가 아닙니다.");
					//return ResponseEntity.badRequest().body(errorResponse);
				}
			}

			List<RoleManager> roleManagers = roleManagerService.findRoleManagerByDeptIdAndUserId(userId, userDeptId);
			List<Integer> roleIds = new ArrayList<>();

			if (roleManagers != null && !roleManagers.isEmpty()) {
				for (RoleManager roleManager : roleManagers) {
					roleIds.add(roleManager.getRoleId());
				}

				if (!roleIds.isEmpty()) {
					int[] roleIdArray = roleIds.stream().mapToInt(Integer::intValue).toArray();
					List<MenuRole> menuRole = menuRoleService.getMenuRolesByRoleIds(roleIdArray);
					response.put("roleList", roleIdArray);
					response.put("menuRoleList", menuRole);
					log.info("롤 조회 및 세팅");
				} else {
					response.put("roleList", new ArrayList<>());
					response.put("menuRoleList", new ArrayList<>());
					log.info("빈값");
				}
			}else{
				roleIds.add(99);
				int[] roleIdArray = roleIds.stream().mapToInt(Integer::intValue).toArray();
				List<MenuRole> menuRole = menuRoleService.getMenuRolesByRoleIds(roleIdArray);
				response.put("roleList", roleIdArray);
				response.put("menuRoleList", menuRole);
				log.info("일반사용자 세팅");
			}

			String userIp = ClientIPFinder.getClientIP(request);

			response.put("userId", userId);
			response.put("companyNm", userData[0].getCompanyName());
			response.put("deptNm", userData[0].getDeptName());
			response.put("deptId", userData[0].getDeptId());
			response.put("userNm", userData[0].getUserName());
			response.put("userIp", userIp);
			response.put("parentDeptId", deptAllList[0].getDeptId());
			response.put("loginTime", LocalDateTime.now());

			log.info("부서정보 검증");
			try {

				LoginLogRequest reqLoginInfo = new LoginLogRequest();
				reqLoginInfo.setUserId(userId);
				reqLoginInfo.setUserNm(userData[0].getUserName());
				reqLoginInfo.setIpAddr(userIp);
				reqLoginInfo.setDeptNm(userData[0].getDeptName());
				reqLoginInfo.setDeptId(userData[0].getDeptId());

				UserLoginHistory savedLog = loginLogService.saveLoginHistory(reqLoginInfo);
				log.info("로그인정보 저장{}", savedLog.toString());

			} catch (IllegalArgumentException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				log.error("error ", e);
				errorResponse.put("error", "로그인 이력 저장 중 오류가 발생했습니다.");
				return ResponseEntity.badRequest().body(errorResponse);
			}
		} catch (Exception e) {
			log.error("조직도 proxy error ", e);
		}

		response.put("userId", userId);
		response.put("loginTime", LocalDateTime.now());

        log.info("response : {}", response);
        return ResponseEntity.ok(response);
    }
}
