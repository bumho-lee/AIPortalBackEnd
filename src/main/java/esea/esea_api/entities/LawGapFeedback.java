package esea.esea_api.entities;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@Table(name = "LAW_GAP_FEED_BACK")
public class LawGapFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LAW_GAP_FEED_BACK_ID")
    private Integer lawGapFeedbackId;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "FEEDBACK", nullable = true)
    private String feedback;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "CREATED_AT", columnDefinition = "TIMESTAMPTZ(0) DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Law_GAP_ID")
    private LawGap lawGap;
}