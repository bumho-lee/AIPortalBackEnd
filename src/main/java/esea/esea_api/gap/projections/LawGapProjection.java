package esea.esea_api.gap.projections;

import java.time.Instant;

public interface LawGapProjection {
    Long getLawGapId();
    String getTitle();
    String getLid();
    String getMst();
    String getAmendment();
    String getAmendmentReason();
    String getParsedArticles();
    String getParsedArticlesText();
    String getAnalysisOpinion();
    String getAnalysisResult();
    String getAnalysisStatus();
    String getReflectYn();
    String getStarredArticles();
    String getAppendixArticles();
    String getRuleData();
    String getEffDts();
    Instant getRegDt();      // OffsetDateTime에서 Instant로 변경
    Instant getCreateDt();   // OffsetDateTime에서 Instant로 변경
    Instant getUpdateDt();   // OffsetDateTime에서 Instant로 변경
    Integer getFeedbackCount();
    String getFeedbackUserIds();
    String getPromulgationName();
    String getLawType();
    String getManagers();
    String getReviewer();
}
