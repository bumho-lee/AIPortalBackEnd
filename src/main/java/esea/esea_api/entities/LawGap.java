package esea.esea_api.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "LAW_GAP")
@Getter
@Setter
public class LawGap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LAW_GAP_ID")
    private Integer lawGapId;

    @Column(name = "TITLE", length = 255)
    private String title;

    @Column(name = "LID", length = 255)
    private String lid;

    @Column(name = "MST", length = 255)
    private String mst;

    @Column(name = "AMENDMENT", columnDefinition = "TEXT")
    private String amendment;

    @Column(name = "AMENDMENT_REASON", columnDefinition = "TEXT")
    private String amendmentReason;

    @Column(name = "PARSED_ARTICLES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String parsedArticles;

    @Column(name = "PARSED_ARTICLES_TEXT", columnDefinition = "TEXT")
    private String parsedArticlesText;

    @Column(name = "ANALYSIS_OPINION", columnDefinition = "TEXT")
    private String analysisOpinion;

    @Column(name = "ANALYSIS_RESULT", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String analysisResult;

    @Column(name = "ANALYSIS_STATUS", columnDefinition = "TEXT")
    private String analysisStatus;

    @Column(name = "REFLECT_YN", columnDefinition = "TEXT")
    private String reflectYn = "N";

    @Column(name = "STARRED_ARTICLES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String starredArticles;

    @Column(name = "APPENDIX_ARTICLES", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String appendixArticles;

    @Column(name = "EFF_DTS")
    private List<OffsetDateTime> effDts;

    @Column(name = "REG_DT")
    private OffsetDateTime regDt;

    @Column(name = "CREATE_DT")
    private OffsetDateTime createDt;

    @Column(name = "UPDATE_DT")
    private OffsetDateTime updateDt;

    @Column(name = "PROMULGATION_NAME")
    private String promulgationName;

    @Column(name = "TOP_LAW_NAME")
    private String topLawName;

    @Column(name = "LAW_TYPE")
    private String lawType;

    @OneToMany(mappedBy = "lawGap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LawGapFeedback> feedbacks;

    @Column(name = "RULE_DATA", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String ruleData;

    @Column(name = "MANAGERS", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String managers;

    @Column(name = "REVIEWER", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String reviewer;
}
