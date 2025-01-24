package esea.esea_api.repositories;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import esea.esea_api.entities.DailyLlmUserUsage;
import esea.esea_api.statistics.dto.DailyLlmUserUsageDto;

@Repository
public interface DailyLlmUserUsageRepository extends JpaRepository<DailyLlmUserUsage, Integer> {
    @Query(
        value = "SELECT " +
                "d.\"USER_ID\" as USER_ID, " +
                "d.\"LLM_MODEL\" as LLM_MODEL, " +
                "SUM(d.\"COUNT\") as COUNT, " +
                "SUM(d.\"INPUT_USAGE\") as INPUT_USAGE, " +
                "SUM(d.\"OUTPUT_USAGE\") as OUTPUT_USAGE " +
                "FROM \"DAILY_LLM_USER_USAGE\" d " +
                "WHERE (CAST(:startDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR d.\"DATE\" >= :startDate) " +
                "AND (CAST(:endDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR d.\"DATE\" <= :endDate) " +
                "AND (CAST(:model AS VARCHAR) IS NULL OR d.\"LLM_MODEL\" = :model) " +
                "AND (CAST(:userId AS VARCHAR) IS NULL OR d.\"USER_ID\" = :userId) " +
                "AND (CAST(:deptId AS VARCHAR) IS NULL OR d.\"DEPT_ID\" = :deptId) " +
                "AND d.\"PURPOSE\" = 'CHAT' " +
                "GROUP BY d.\"USER_ID\", d.\"LLM_MODEL\" " +
                "ORDER BY SUM(d.\"COUNT\") DESC",
        countQuery = "SELECT COUNT(DISTINCT d.\"USER_ID\") FROM \"DAILY_LLM_USER_USAGE\" d " +
                    "WHERE (CAST(:startDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR d.\"DATE\" >= :startDate) " +
                    "AND (CAST(:endDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR d.\"DATE\" <= :endDate) " +
                    "AND (CAST(:model AS VARCHAR) IS NULL OR d.\"LLM_MODEL\" = :model) " +
                    "AND (CAST(:userId AS VARCHAR) IS NULL OR d.\"USER_ID\" = :userId) " +
                    "AND (CAST(:deptId AS VARCHAR) IS NULL OR d.\"DEPT_ID\" = :deptId) " +
                    "AND d.\"PURPOSE\" = 'CHAT'",
        nativeQuery = true
    )
    Page<DailyLlmUserUsageDto> searchUserUsageGroupByUser(
        @Param("startDate") OffsetDateTime startDate,
        @Param("endDate") OffsetDateTime endDate,
        @Param("userId") String userId,
        @Param("deptId") String deptId,
        @Param("model") String model,
        Pageable pageable
    );

    @Query(
        value = "SELECT " +
                "d.\"USER_ID\" as USER_ID, " +
                "d.\"LLM_MODEL\" as LLM_MODEL, " +
                "SUM(d.\"COUNT\") as COUNT, " +
                "SUM(d.\"INPUT_USAGE\") as INPUT_USAGE, " +
                "SUM(d.\"OUTPUT_USAGE\") as OUTPUT_USAGE " +
                "FROM \"DAILY_LLM_USER_USAGE\" d " +
                "WHERE (CAST(:startDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR d.\"DATE\" >= :startDate) " +
                "AND (CAST(:endDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR d.\"DATE\" <= :endDate) " +
                "AND (CAST(:model AS VARCHAR) IS NULL OR d.\"LLM_MODEL\" = :model) " +
                "AND (CAST(:userId AS VARCHAR) IS NULL OR d.\"USER_ID\" = :userId) " +
                "AND (CAST(:deptId AS VARCHAR) IS NULL OR d.\"DEPT_ID\" = :deptId) " +
                "AND d.\"PURPOSE\" = 'CHAT' " +
                "GROUP BY d.\"USER_ID\", d.\"LLM_MODEL\" " +
                "ORDER BY SUM(d.\"COUNT\") DESC",
        nativeQuery = true
    )
    List<DailyLlmUserUsageDto> searchUserUsageGroupByUserList(@Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate, @Param("userId") String userId, @Param("deptId") String deptId, @Param("model") String model);
}
