package esea.esea_api.admin.dashboard.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DAILY_LLM_USAGE")
@Getter
@Setter
public class DailyLlmUsage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DAILY_USAGE_ID")
    private int dailyUsageId;

    @Column(name = "LLM_MODEL", nullable = false)
    private String llmModel;

    @Column(name = "INPUT_USAGE", nullable = false)
    private int inputUsage;

    @Column(name = "OUTPUT_USAGE")
    private int outputUsage;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "PURPOSE")
    private String purpose;

}
