package esea.esea_api.gap;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import esea.esea_api.entities.LawGap;
import esea.esea_api.gap.projections.LawGapProjection;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.data.jpa.repository.Modifying;

@Repository
public interface GapRepository extends JpaRepository<LawGap, Integer> {
        Optional<LawGap> findByLawGapId(Integer lawGapId);

        @Query(value = "WITH feedback_stats AS (" +
               "    SELECT f.\"Law_GAP_ID\", " +
               "           COUNT(*) as feedback_count, " +
               "           STRING_AGG(DISTINCT f.\"USER_ID\"::text, ',') as feedback_user_ids " +
               "    FROM \"LAW_GAP_FEED_BACK\" f " +
               "    GROUP BY f.\"Law_GAP_ID\" " +
               ") " +
               "SELECT lg.\"LAW_GAP_ID\" as lawGapId, " +
               "       lg.\"TITLE\" as title, " +
               "       lg.\"LID\" as lid, " +
               "       lg.\"MST\" as mst, " +
               "       lg.\"AMENDMENT\" as amendment, " +
               "       lg.\"AMENDMENT_REASON\" as amendmentReason, " +
               "       lg.\"PARSED_ARTICLES\"::text as parsedArticles, " +
               "       lg.\"PARSED_ARTICLES_TEXT\" as parsedArticlesText, " +
               "       lg.\"ANALYSIS_OPINION\" as analysisOpinion, " +
               "       lg.\"ANALYSIS_RESULT\"::text as analysisResult, " +
               "       lg.\"ANALYSIS_STATUS\" as analysisStatus, " +
               "       lg.\"REFLECT_YN\" as reflectYn, " +
               "       lg.\"STARRED_ARTICLES\"::text as starredArticles, " +
               "       lg.\"APPENDIX_ARTICLES\"::text as appendixArticles, " +
               "       lg.\"RULE_DATA\"::text as ruleData, " +
               "       array_to_string(lg.\"EFF_DTS\", ',') as effDts, " +
               "       lg.\"REG_DT\"::timestamp as regDt, " +
               "       lg.\"CREATE_DT\"::timestamp as createDt, " +
               "       lg.\"UPDATE_DT\"::timestamp as updateDt, " +
               "       COALESCE(fs.feedback_count, 0) as feedbackCount, " +
               "       fs.feedback_user_ids as feedbackUserIds, " +
               "       lg.\"PROMULGATION_NAME\" as promulgationName, " +
               "       lg.\"LAW_TYPE\" as lawType, " +
               "       lg.\"MANAGERS\" as managers, " +
               "       lg.\"REVIEWER\" as reviewer " +
               "FROM \"LAW_GAP\" lg " +
               "LEFT JOIN feedback_stats fs ON lg.\"LAW_GAP_ID\" = fs.\"Law_GAP_ID\" " +
               "WHERE (CAST(:startDate AS timestamptz) IS NULL OR " +
                    "lg.\"REG_DT\" >= CAST(:startDate AS timestamptz)) " +
               "AND (CAST(:endDate AS timestamptz) IS NULL OR " +
                    "lg.\"REG_DT\" <= CAST(:endDate AS timestamptz)) " +
               "AND (CAST(:keyword AS text) IS NULL OR " +
                    "lg.\"TITLE\" LIKE '%' || CAST(:keyword AS text) || '%' OR " +
                    "lg.\"ANALYSIS_RESULT\"::text LIKE '%' || CAST(:keyword AS text) || '%') " +
               "AND (:feedbackYn IS NULL OR " +
                    "(:feedbackYn = 'Y' AND COALESCE(fs.feedback_count, 0) > 0) OR " +
                    "(:feedbackYn = 'N' AND COALESCE(fs.feedback_count, 0) = 0)) " +
               "AND (:managerName IS NULL OR " +
                    "lg.\"MANAGERS\"::text LIKE '%' || CAST(:managerName AS text) || '%') " +
               "AND (:reviewerName IS NULL OR " +
                    "lg.\"REVIEWER\"::text LIKE '%' || CAST(:reviewerName AS text) || '%') " +
               "ORDER BY lg.\"REG_DT\" DESC",
               countQuery = "WITH feedback_stats AS (" +
                       "    SELECT f.\"Law_GAP_ID\", " +
                       "           COUNT(f.\"FEEDBACK\") as feedback_count, " +
                       "           STRING_AGG(DISTINCT f.\"USER_ID\"::text, ',') as feedback_user_ids " +
                       "    FROM \"LAW_GAP_FEED_BACK\" f " +
                       "    GROUP BY f.\"Law_GAP_ID\" " +
                       ") " +
                       "SELECT COUNT(*) FROM \"LAW_GAP\" lg " +
                       "LEFT JOIN feedback_stats fs ON lg.\"LAW_GAP_ID\" = fs.\"Law_GAP_ID\" " +
                       "WHERE (CAST(:startDate AS timestamptz) IS NULL OR " +
                            "lg.\"REG_DT\" >= CAST(:startDate AS timestamptz)) " +
                       "AND (CAST(:endDate AS timestamptz) IS NULL OR " +
                            "lg.\"REG_DT\" <= CAST(:endDate AS timestamptz)) " +
                       "AND (CAST(:keyword AS text) IS NULL OR " +
                            "lg.\"TITLE\" LIKE '%' || CAST(:keyword AS text) || '%' OR " +
                            "lg.\"ANALYSIS_RESULT\"::text LIKE '%' || CAST(:keyword AS text) || '%') " +
                       "AND (:feedbackYn IS NULL OR " +
                            "(:feedbackYn = 'Y' AND COALESCE(fs.feedback_count, 0) > 0) OR " +
                            "(:feedbackYn = 'N' AND COALESCE(fs.feedback_count, 0) = 0))",
               nativeQuery = true)
        Page<LawGapProjection> searchLawGaps(
                @Param("startDate") OffsetDateTime startDate,
                @Param("endDate") OffsetDateTime endDate,
                @Param("keyword") String keyword,
                @Param("feedbackYn") String feedbackYn,
                @Param("managerName") String managerName,
                @Param("reviewerName") String reviewerName,
                Pageable pageable
        );

        @Query(value = "WITH feedback_stats AS (" +
               "    SELECT f.\"Law_GAP_ID\", " +
               "           COUNT(*) as feedback_count, " +
               "           STRING_AGG(DISTINCT f.\"USER_ID\"::text, ',') as feedback_user_ids " +
               "    FROM \"LAW_GAP_FEED_BACK\" f " +
               "    GROUP BY f.\"Law_GAP_ID\" " +
               ") " +
               "SELECT lg.\"LAW_GAP_ID\" as lawGapId, " +
               "       lg.\"TITLE\" as title, " +
               "       lg.\"LID\" as lid, " +
               "       lg.\"MST\" as mst, " +
               "       lg.\"AMENDMENT\" as amendment, " +
               "       lg.\"AMENDMENT_REASON\" as amendmentReason, " +
               "       lg.\"PARSED_ARTICLES\"::text as parsedArticles, " +
               "       lg.\"PARSED_ARTICLES_TEXT\" as parsedArticlesText, " +
               "       lg.\"ANALYSIS_OPINION\" as analysisOpinion, " +
               "       lg.\"ANALYSIS_RESULT\"::text as analysisResult, " +
               "       lg.\"ANALYSIS_STATUS\" as analysisStatus, " +
               "       lg.\"REFLECT_YN\" as reflectYn, " +
               "       lg.\"STARRED_ARTICLES\"::text as starredArticles, " +
               "       lg.\"APPENDIX_ARTICLES\"::text as appendixArticles, " +
               "       lg.\"RULE_DATA\"::text as ruleData, " +
               "       array_to_string(lg.\"EFF_DTS\", ',') as effDts, " +
               "       lg.\"REG_DT\"::timestamp as regDt, " +
               "       lg.\"CREATE_DT\"::timestamp as createDt, " +
               "       lg.\"UPDATE_DT\"::timestamp as updateDt, " +
               "       COALESCE(fs.feedback_count, 0) as feedbackCount, " +
               "       fs.feedback_user_ids as feedbackUserIds, " +
               "       lg.\"PROMULGATION_NAME\" as promulgationName, " +
               "       lg.\"LAW_TYPE\" as lawType, " +
               "       lg.\"MANAGERS\" as managers, " +
               "       lg.\"REVIEWER\" as reviewer " +
               "FROM \"LAW_GAP\" lg " +
               "LEFT JOIN feedback_stats fs ON lg.\"LAW_GAP_ID\" = fs.\"Law_GAP_ID\" " +
               "WHERE (CAST(:startDate AS timestamptz) IS NULL OR " +
                    "lg.\"REG_DT\" >= CAST(:startDate AS timestamptz)) " +
               "AND (CAST(:endDate AS timestamptz) IS NULL OR " +
                    "lg.\"REG_DT\" <= CAST(:endDate AS timestamptz)) " +
               "AND (CAST(:keyword AS text) IS NULL OR " +
                    "lg.\"TITLE\" LIKE '%' || CAST(:keyword AS text) || '%' OR " +
                    "lg.\"ANALYSIS_RESULT\"::text LIKE '%' || CAST(:keyword AS text) || '%') " +
               "AND (:feedbackYn IS NULL OR " +
                    "(:feedbackYn = 'Y' AND COALESCE(fs.feedback_count, 0) > 0) OR " +
                    "(:feedbackYn = 'N' AND COALESCE(fs.feedback_count, 0) = 0)) " +
               "AND (:managerName IS NULL OR " +
                    "lg.\"MANAGERS\"::text LIKE '%' || CAST(:managerName AS text) || '%') " +
               "AND (:reviewerName IS NULL OR " +
                    "lg.\"REVIEWER\"::text LIKE '%' || CAST(:reviewerName AS text) || '%') " +
               "ORDER BY lg.\"REG_DT\" DESC", nativeQuery = true)
        List<LawGapProjection> searchLawGapsWithoutPaging(
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            @Param("keyword") String keyword,
            @Param("feedbackYn") String feedbackYn,
            @Param("managerName") String managerName,
            @Param("reviewerName") String reviewerName
        );

        @Modifying
        @Query(value = "UPDATE \"LAW_GAP\" lg SET \"REVIEWER\" = CAST(:reviewer AS jsonb) WHERE lg.\"LAW_GAP_ID\" = :lawGapId", nativeQuery = true)
        void setReviewer(@Param("lawGapId") Integer lawGapId, @Param("reviewer") String reviewer);
}
