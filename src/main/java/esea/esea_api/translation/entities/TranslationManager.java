package esea.esea_api.translation.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TRANSLATION_MANAGER")
@Getter
@Setter
@NoArgsConstructor
public class TranslationManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSLATION_ID")
    private int translationId;

    @Column(name = "API_KEY", nullable = false)
    private String apiKey;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "DEPARTMENT_ID")
    private String department_id;
    
    @Column(name = "TRANSLATION_LIMIT")
    private int translationLimit;

    @Column(name = "TRANSLATION_USAGE")
    private int translationUsage;

    @Column(name = "TRANSLATION_USAGE_RATE")
    private BigDecimal translationUsageRate;
    
    @Column(name = "COST")
    private BigDecimal cost;

    @Column(name = "REG_ID", nullable = false)
    private String regId;

    @Column(name = "REG_DT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDt = LocalDateTime.now(); // 업로드한 시간
    
    @Column(name = "UPDATE_DT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDt; // 업로드한 시간
    
}
