package esea.esea_api.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "DAILY_LLM_USER_USAGE")
public class DailyLlmUserUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DAILY_LLM_USER_USAGE_ID")
    private Integer dailyLlmUserUsageId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "COUNT")
    private Integer count;

    @Column(name = "LLM_MODEL")
    private String llmModel;

    @Column(name = "INPUT_USAGE")
    private Integer inputUsage;

    @Column(name = "OUTPUT_USAGE")
    private Integer outputUsage;

    @Column(name = "PURPOSE")
    private String purpose;

    @Column(name = "DATE")
    private LocalDate date;
}
