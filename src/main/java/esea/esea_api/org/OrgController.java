package esea.esea_api.org;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import esea.esea_api.org.dto.OrgRequestDto;
import esea.org.service.NeoOrgWsProxy;
import esea.org.vo.OrgDeptVO;
import esea.org.vo.OrgUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ORG", description = "ORG")
@RestController
@RequestMapping("/org")
public class OrgController {
    @Operation(summary = "이름/부서명 조회")
    @PostMapping(value = "/search")
    public ResponseEntity<?> search(
    		@RequestBody OrgRequestDto orgRequestDto) throws RemoteException{
    	Map<String, Object> response = new HashMap<>();
    	
    	NeoOrgWsProxy proxy = new NeoOrgWsProxy();

    	try { 
        // 사용자 데이터 처리
        OrgUserVO[] userList = proxy.searchUserByUserName(orgRequestDto.getSearchText());
        if (userList != null && userList.length > 0) {
            Map<String, Object> userResponse = new HashMap<>();
            response.put("type", "user");

            List<Map<String, Object>> userContents = new ArrayList<>();
            for (OrgUserVO user : userList) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userName", user.getUserName());
                userMap.put("userId", user.getUserId());
                userMap.put("deptId", user.getDeptId());
                userMap.put("deptCode", user.getDeptCode());
                userMap.put("companyId", user.getCompanyId());
                userMap.put("deptName", user.getDeptName());
                userMap.put("jobPositionName", user.getJobPositionName());
                userMap.put("email", user.getEmail() != null ? user.getEmail() : "");
                userContents.add(userMap);
            }

            response.put("contents", userContents);
        }

        // 부서 데이터 처리
        OrgDeptVO[] deptList = proxy.searchDeptByDeptName(orgRequestDto.getSearchText());
        if (deptList != null && deptList.length > 0) {
            Map<String, Object> deptResponse = new HashMap<>();
            response.put("type", "dept");

            List<Map<String, Object>> deptContents = new ArrayList<>();
            for (OrgDeptVO dept : deptList) {
                Map<String, Object> deptMap = new HashMap<>();
                deptMap.put("userName", ""); // 부서에는 사용자 정보 없음
                deptMap.put("userId", "");   // 부서에는 사용자 ID 정보 없음
                deptMap.put("deptId", dept.getDeptId());
                deptMap.put("deptCode", dept.getDeptCode());
                deptMap.put("companyId", dept.getCompanyId());
                deptMap.put("deptName", dept.getDeptName());
                deptMap.put("jobPositionName", ""); // 부서에는 직책 정보 없음
                deptContents.add(deptMap);
            }
            response.put("contents", deptContents);
        }
        }catch (Exception e) {
        	response.put("error", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @Operation(summary = "이름 조회")
    @PostMapping(value = "/searchUser")
    public ResponseEntity<?> searchUser(
    		@RequestBody OrgRequestDto orgRequestDto){
    	Map<String, Object> response = new HashMap<>();
    	
    	NeoOrgWsProxy proxy = new NeoOrgWsProxy();

    	try {    	
	        // 사용자 데이터 처리
	        OrgUserVO[] userList = proxy.searchUserByUserName(orgRequestDto.getSearchText());
	        if (userList != null && userList.length > 0) {
	            Map<String, Object> userResponse = new HashMap<>();
	            response.put("type", "user");
	
	            List<Map<String, Object>> userContents = new ArrayList<>();
	            for (OrgUserVO user : userList) {
	                Map<String, Object> userMap = new HashMap<>();
	                userMap.put("userName", user.getUserName());
	                userMap.put("userId", user.getUserId());
	                userMap.put("deptId", user.getDeptId());
	                userMap.put("deptCode", user.getDeptCode());
	                userMap.put("deptName", user.getDeptName());
	                userMap.put("jobPositionName", user.getJobPositionName());
	                userContents.add(userMap);
	            }
	
	            response.put("contents", userContents);
	            
	        }
        }catch (Exception e) {
        	response.put("error", e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
     
    @Operation(summary = "부서명 조회")
    @PostMapping(value = "/searchDept")
    public ResponseEntity<?> searchDept(
    		@RequestBody OrgRequestDto orgRequestDto) throws RemoteException{
    	Map<String, Object> response = new HashMap<>();
    	
    	NeoOrgWsProxy proxy = new NeoOrgWsProxy();

    	try { 
        // 부서 데이터 처리
        OrgDeptVO[] deptList = proxy.searchDeptByDeptName(orgRequestDto.getSearchText());
        if (deptList != null && deptList.length > 0) {
            Map<String, Object> deptResponse = new HashMap<>();
            response.put("type", "dept");

            List<Map<String, Object>> deptContents = new ArrayList<>();
            for (OrgDeptVO dept : deptList) {
                Map<String, Object> deptMap = new HashMap<>();
                deptMap.put("deptId", dept.getDeptId());
                deptMap.put("deptCode", dept.getDeptCode());
                deptMap.put("deptName", dept.getDeptName());
                deptContents.add(deptMap);
            }

            response.put("contents", deptContents);
        }
    }catch (Exception e) {
    	response.put("error", e.getMessage());
    }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @Operation(summary = "조직정보 조회")
    @PostMapping(value = "/searchDeptAll")
    public ResponseEntity<?> searchDeptAll(
    		@RequestBody OrgRequestDto orgRequestDto) throws RemoteException{
    	Map<String, Object> response = new HashMap<>();
    	
    	NeoOrgWsProxy proxy = new NeoOrgWsProxy();
    	
    	try { 
        // 부서 데이터 처리
        OrgDeptVO[] deptList = proxy.searchDeptByDeptIdIncludeChild(orgRequestDto.getSearchText());
        if (deptList != null && deptList.length > 0) {

            response.put("contents", deptList);
        }
    }catch (Exception e) {
    	e.printStackTrace();
    	response.put("error", e.getMessage());
    }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

