package esea.esea_api.admin.login.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import esea.esea_api.admin.login.dto.LoginLogRequest;
import esea.esea_api.admin.login.entities.UserLoginHistory;
import esea.esea_api.admin.login.repository.LoginLogRepository;
import jakarta.transaction.Transactional;

@Service
public class LoginLogService {
	
    private final LoginLogRepository loginLogRepository;
    
    @Autowired
    public LoginLogService(LoginLogRepository loginLogRepository) {
        this.loginLogRepository = loginLogRepository;
    }
    
    public Page<UserLoginHistory> getLoginHistory(String start, String end, String userNm, String deptNm, int page, int size) {
    	
    	PageRequest pageRequest = PageRequest.of(page -1, size);
    	
        return loginLogRepository.findLoginLog(start, end, userNm != null ? userNm : "" , deptNm != null ? deptNm : "" , pageRequest);
    }
    
    /**
     * 로그인 이력 저장
     */
    @Transactional
    public UserLoginHistory saveLoginHistory(LoginLogRequest requestDto) {
        try {
        	UserLoginHistory loginLog = new UserLoginHistory();
        	loginLog.setUserId(requestDto.getUserId());
        	loginLog.setUserNm(requestDto.getUserNm());
        	loginLog.setIpAddr(requestDto.getIpAddr());
        	loginLog.setDeptNm(requestDto.getDeptNm());
        	loginLog.setDeptId(requestDto.getDeptId());
        	loginLog.setLoginYn("Y");
        	loginLog.setLoginTime(LocalDateTime.now());

            return loginLogRepository.save(loginLog);
        } catch (Exception e) {
            throw new RuntimeException("로그인 이력 저장 중 오류가 발생했습니다.", e);
        }
    }

}
