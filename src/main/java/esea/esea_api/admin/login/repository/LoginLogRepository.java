package esea.esea_api.admin.login.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import esea.esea_api.admin.login.entities.UserLoginHistory;

@Repository
public interface LoginLogRepository extends JpaRepository<UserLoginHistory, Integer> {
	
    // 특정 기간 동안의 로그인 이력 조회
    @Query(value ="SELECT * FROM \"USER_LOGIN_HISTORY\" "
    		+ "WHERE TO_CHAR(\"LOGIN_TIME\", 'YYYYMMDD') BETWEEN :startDate AND :endDate"
    		+ " AND \"USER_NM\" ILIKE CONCAT('%', :userNm, '%') "
    		+ " AND \"DEPT_NM\" ILIKE CONCAT('%', :deptNm, '%') "
   		 	+ "ORDER BY \"LOGIN_TIME\" DESC", nativeQuery = true)
    Page<UserLoginHistory> findLoginLog(
            @Param("startDate") String startDateTime,
            @Param("endDate") String endDateTime,
            @Param("userNm") String userNm,
            @Param("deptNm") String deptNm,
            PageRequest pageRequest
        );
}
