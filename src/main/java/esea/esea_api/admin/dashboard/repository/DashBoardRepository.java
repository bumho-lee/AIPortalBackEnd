package esea.esea_api.admin.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import esea.esea_api.admin.dashboard.entities.DailyLlmUsage;

@Repository
public interface DashBoardRepository extends JpaRepository<DailyLlmUsage, Integer>{

    @Query(value ="WITH date_range AS ("+
			     	   "SELECT TO_DATE(:startDate, 'YYYYMMDD') + (i * INTERVAL '1 day') AS LOGIN_DATE "+
			   	    "FROM generate_series(0, (TO_DATE(:endDate, 'YYYYMMDD') - TO_DATE(:startDate, 'YYYYMMDD'))::int) i "+
			   	") "+
			   	"SELECT "+ 
			   	    "TO_CHAR(d.LOGIN_DATE, 'YYYY-MM-DD') AS LOGIN_DATE, "+
			   	    "COUNT(DISTINCT u.\"USER_ID\") AS LOGIN_COUNT "+
			   	"FROM "+ 
			   	    "date_range d "+
			   	"LEFT JOIN "+ 
			   	    "\"USER_LOGIN_HISTORY\" u "+
			   	    "ON u.\"CREATED_AT\"::DATE = d.LOGIN_DATE "+ 
			   	    "AND u.\"LOGIN_YN\" = 'Y' "+
			   	"GROUP BY d.LOGIN_DATE "+
			   	"ORDER BY d.LOGIN_DATE DESC", nativeQuery = true)
    List<Object[]> dashBoardUserWeekCount(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    @Query(value = "WITH date_range AS (" +
            "SELECT TO_DATE(:startDate, 'YYYYMMDD') + (i * INTERVAL '1 day') AS CHAT_DATE " +
            "FROM generate_series(0, (TO_DATE(:endDate, 'YYYYMMDD') - TO_DATE(:startDate, 'YYYYMMDD'))::int) i " +
        ") " +
        "SELECT " +
            "TO_CHAR(d.CHAT_DATE, 'YYYY-MM-DD') AS CHAT_DATE, " +
            "COALESCE(SUM(dcc.\"COUNT\"), 0) AS TOTAL_CHAT_COUNT " +
        "FROM " +
            "date_range d " +
        "LEFT JOIN " +
            "\"DAILY_CHAT_COUNT\" dcc " +
            "ON DATE_TRUNC('day', dcc.\"DATE\") = d.CHAT_DATE " +
        "GROUP BY d.CHAT_DATE " +
        "ORDER BY d.CHAT_DATE DESC", nativeQuery = true)
    List<Object[]> dashBoardChatWeekCount(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    @Query(value ="WITH date_range AS ( "+
		"SELECT TO_CHAR(DATE_TRUNC('month', TO_DATE(:startDate, 'YYYY-MM-DD')) + (i * INTERVAL '1 month'), 'YYYY-MM') AS LOGIN_DATE "+
		"FROM generate_series(0, EXTRACT(YEAR FROM AGE(TO_DATE(:endDate, 'YYYY-MM-DD'), TO_DATE(:startDate, 'YYYY-MM-DD'))) * 12 + EXTRACT(MONTH FROM AGE(TO_DATE(:endDate, 'YYYY-MM-DD'), TO_DATE(:startDate, 'YYYY-MM-DD')))) AS gs(i) "+
		") "+
		"SELECT "+ 
			"dr.LOGIN_DATE AS LOGIN_MONTH, "+
			"COUNT(DISTINCT u.\"USER_ID\") AS LOGIN_COUNT "+
		"FROM "+ 
			"date_range dr "+
		"LEFT JOIN "+ 
			"\"USER_LOGIN_HISTORY\" u "+
			"ON TO_CHAR(u.\"CREATED_AT\", 'YYYY-MM') = dr.LOGIN_DATE "+
			"AND u.\"LOGIN_YN\" = 'Y' "+
		"GROUP BY dr.LOGIN_DATE "+
		"ORDER BY dr.LOGIN_DATE DESC", nativeQuery = true)
	List<Object[]> dashBoardUserMonthCount(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value ="WITH date_range AS ( "+
	    "SELECT TO_CHAR(DATE_TRUNC('month', TO_DATE(:startDate, 'YYYY-MM-DD')) + (i * INTERVAL '1 month'), 'YYYY-MM') AS MONTH "+
	    "FROM generate_series(0, EXTRACT(YEAR FROM AGE(TO_DATE(:endDate, 'YYYY-MM-DD'), TO_DATE(:startDate, 'YYYY-MM-DD'))) * 12 + EXTRACT(MONTH FROM AGE(TO_DATE(:endDate, 'YYYY-MM-DD'), TO_DATE(:startDate, 'YYYY-MM-DD')))) AS gs(i) "+
		") "+
		"SELECT "+ 
		    "dr.MONTH, "+
		    "COALESCE(SUM(dcc.\"COUNT\"), 0) AS TOTAL_CHAT_COUNT "+
		"FROM "+ 
		    "date_range dr "+
		"LEFT JOIN "+ 
		    "\"DAILY_CHAT_COUNT\" dcc "+ 
		    "ON TO_CHAR(dcc.\"DATE\", 'YYYY-MM') = dr.MONTH "+
		"GROUP BY dr.MONTH "+
		"ORDER BY dr.MONTH DESC", nativeQuery = true)
	List<Object[]> dashBoardChatMonthCount(@Param("startDate") String startDate, @Param("endDate") String endDate);

	@Query(value = "SELECT " +
        "TO_CHAR(gs.date, 'YYYY-MM') AS LOGIN_MONTH, " +
        "COUNT(DISTINCT u.\"USER_ID\") AS LOGIN_COUNT " +
        "FROM " +
        "generate_series(DATE_TRUNC('month', TO_DATE(:sMonth, 'YYYYMM')), DATE_TRUNC('month', TO_DATE(:eMonth, 'YYYYMM')), '1 month'::INTERVAL) AS gs(date) " +
        "LEFT JOIN " +
        "\"USER_LOGIN_HISTORY\" u " +
        "ON DATE_TRUNC('month', u.\"CREATED_AT\") = gs.date " +
        "AND u.\"LOGIN_YN\" = 'Y' " +
        "GROUP BY LOGIN_MONTH " +
        "ORDER BY LOGIN_MONTH DESC",	nativeQuery = true)
	List<Object[]> dashBoardUserCount(@Param("sMonth") String sMonth, @Param("eMonth") String eMonth);

	@Query(value = "WITH date_range AS ( " +
		"SELECT TO_CHAR(DATE_TRUNC('month', TO_DATE(:sMonth, 'YYYYMM')) + (i * INTERVAL '1 month'), 'YYYY-MM') AS MONTH " +
		"FROM generate_series(0, " +
		"  (EXTRACT(YEAR FROM TO_DATE(:eMonth, 'YYYYMM')) - EXTRACT(YEAR FROM TO_DATE(:sMonth, 'YYYYMM'))) * 12 + " +
		"  (EXTRACT(MONTH FROM TO_DATE(:eMonth, 'YYYYMM')) - EXTRACT(MONTH FROM TO_DATE(:sMonth, 'YYYYMM'))) " +
		") AS gs(i) " +
		") " +
		"SELECT " +
		"dr.MONTH, " +
		"COALESCE(SUM(dcc.\"COUNT\"), 0) AS TOTAL_CHAT_COUNT " +
		"FROM " +
		"date_range dr " +
		"LEFT JOIN " +
		"\"DAILY_CHAT_COUNT\" dcc " +
		"ON TO_CHAR(dcc.\"DATE\", 'YYYY-MM') = dr.MONTH " +
		"GROUP BY dr.MONTH " +
		"ORDER BY dr.MONTH DESC", nativeQuery = true
	)
	List<Object[]> dashBoardChatCount(@Param("sMonth") String sMonth, @Param("eMonth") String eMonth);
	
	@Query(value ="WITH date_range AS ( "+
		    "SELECT "+ 
	        "(TO_DATE(:startDate, 'YYYY-MM-DD') + (i * INTERVAL '1 month'))::DATE AS MONTH "+
	    "FROM generate_series(0, EXTRACT(YEAR FROM AGE(TO_DATE(:endDate, 'YYYY-MM-DD'), TO_DATE(:startDate, 'YYYY-MM-DD'))) * 12 + EXTRACT(MONTH FROM AGE(TO_DATE(:endDate, 'YYYY-MM-DD'), TO_DATE(:startDate, 'YYYY-MM-DD')))) AS gs(i) "+
		") "+
		"SELECT "+ 
		    "TO_CHAR(dr.MONTH, 'YYYY-MM') AS USAGE_MONTH, "+
		    "dl.\"LLM_MODEL\", "+ 
		    "COALESCE(SUM(dl.\"INPUT_USAGE\"), 0) AS TOTAL_INPUT_USAGE, "+
		    "COALESCE(SUM(dl.\"OUTPUT_USAGE\"), 0) AS TOTAL_OUTPUT_USAGE, "+
		    "COALESCE(SUM(CASE WHEN dl.\"PURPOSE\" = 'GAP' THEN dl.\"INPUT_USAGE\" + dl.\"OUTPUT_USAGE\" ELSE 0 END ), 0) AS RECORD_COUNT, "+
		    "COALESCE(SUM(dl.\"INPUT_USAGE\"), 0) + COALESCE(SUM(dl.\"OUTPUT_USAGE\"), 0) AS TOTAL_SUM "+
		"FROM "+ 
		    "date_range dr "+
		"LEFT JOIN "+ 
		    "\"DAILY_LLM_USAGE\" dl "+
		    "ON TO_CHAR(dl.\"DATE\", 'YYYY-MM') = TO_CHAR(dr.MONTH, 'YYYY-MM') "+
		    "AND dl.\"LLM_MODEL\" = :llmModel "+
		"GROUP BY USAGE_MONTH, dl.\"LLM_MODEL\" "+
		"ORDER BY USAGE_MONTH DESC, dl.\"LLM_MODEL\"", nativeQuery = true)
	List<Object[]> dashBoardTokenCount(@Param("llmModel") String llmModel, @Param("startDate") String startDate, @Param("endDate") String endDate);
    
	@Query(value ="WITH date_range AS ( "+
		"SELECT "+ 
		"(TO_DATE(:sMonth, 'YYYYMM') + (i * INTERVAL '1 month'))::DATE AS MONTH "+
		"FROM "+ 
		"generate_series(0, EXTRACT(YEAR FROM AGE(TO_DATE(:eMonth, 'YYYYMM'), TO_DATE(:sMonth, 'YYYYMM'))) * 12 + EXTRACT(MONTH FROM AGE(TO_DATE(:eMonth, 'YYYYMM'), TO_DATE(:sMonth, 'YYYYMM')))) AS gs(i) "+
		") "+
		"SELECT "+ 
		"TO_CHAR(m.MONTH, 'YYYY-MM') AS regDt, "+
		"COALESCE(SUM(t.\"TRANSLTED_CHARCNT\"), 0) AS translatedCharcnt "+
		"FROM "+ 
		"date_range m "+
		"LEFT JOIN "+ 
		"\"TRANSLATION_FILES\" t "+
		"ON TO_CHAR(t.\"REG_DT\", 'YYYY-MM') = TO_CHAR(m.MONTH, 'YYYY-MM') "+  // 조건을 ON 절로 이동
		"GROUP BY m.MONTH " +
		"ORDER BY m.MONTH DESC", nativeQuery = true)
    List<Object[]> findMonthlyTranslationSummary(@Param("sMonth") String sMonth, @Param("eMonth") String eMonth);
}
