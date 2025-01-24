package esea.esea_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import esea.esea_api.entities.ConversationLog;
import java.time.OffsetDateTime;
import java.util.List;
@Repository
public interface ConversationLogRepository extends JpaRepository<ConversationLog, Integer> {
    
    @Query(
        value = "SELECT * " +
                "FROM \"CONVERSATION_LOG\" c " +
                "WHERE (CAST(:startDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR c.\"REG_DT\" >= :startDate) " +
                "AND (CAST(:endDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR c.\"REG_DT\" <= :endDate) " +
                "AND (CAST(:userId AS VARCHAR) IS NULL OR c.\"USER_ID\" = :userId) " +
                "AND (CAST(:model AS VARCHAR) IS NULL OR c.\"LLM_MODEL\" = :model) " +
                "AND (CAST(:type AS VARCHAR) IS NULL OR c.\"TYPE\" = :type) " +
                "ORDER BY c.\"REG_DT\" DESC",
        countQuery = "SELECT COUNT(*) " +
                     "FROM \"CONVERSATION_LOG\" c " +
                     "WHERE (CAST(:startDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR c.\"REG_DT\" >= :startDate) " +
                     "AND (CAST(:endDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR c.\"REG_DT\" <= :endDate) " +
                     "AND (CAST(:userId AS VARCHAR) IS NULL OR c.\"USER_ID\" = :userId) " +
                     "AND (CAST(:model AS VARCHAR) IS NULL OR c.\"LLM_MODEL\" = :model) " +
                     "AND (CAST(:type AS VARCHAR) IS NULL OR c.\"TYPE\" = :type)",
        nativeQuery = true
    )
    Page<ConversationLog> searchConversationLogs(
        @Param("startDate") OffsetDateTime startDate,
        @Param("endDate") OffsetDateTime endDate,
        @Param("userId") String userId,
        @Param("model") String model,
        @Param("type") String type,
        Pageable pageable
    );

    @Query(
        value = "SELECT * " +
                "FROM \"CONVERSATION_LOG\" c " +
                "WHERE (CAST(:startDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR c.\"REG_DT\" >= :startDate) " +
                "AND (CAST(:endDate AS TIMESTAMP WITH TIME ZONE) IS NULL OR c.\"REG_DT\" <= :endDate) " +
                "AND (CAST(:userId AS VARCHAR) IS NULL OR c.\"USER_ID\" = :userId) " +
                "AND (CAST(:model AS VARCHAR) IS NULL OR c.\"LLM_MODEL\" = :model) " +
                "AND (CAST(:type AS VARCHAR) IS NULL OR c.\"TYPE\" = :type) " +
                "ORDER BY c.\"REG_DT\" DESC",
        nativeQuery = true
    )
    List<ConversationLog> searchConversationLogsList(
        @Param("startDate") OffsetDateTime startDate,
        @Param("endDate") OffsetDateTime endDate,
        @Param("userId") String userId,
        @Param("model") String model,
        @Param("type") String type
    );
}
