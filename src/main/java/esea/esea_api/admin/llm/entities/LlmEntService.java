package esea.esea_api.admin.llm.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LLM_SERVICE")
public class LlmEntService {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LLM_SERVICE_ID")
	private int llmServiceId;

    @Column(name = "NAME", nullable = true)
    private String name;

    @Column(name = "SERVICE_KEY")
    private String serviceKey;

    @Column(name = "STATUS", nullable = true)
    private String status;

    @Column(name = "ICON_PATH", nullable = true)
    private String iconPath;
    
    @Column(name = "ICON_URL")
    private String iconUrl;

    @Column(name = "NOTE", nullable = true)
    private String note;

    @Column(name = "REG_DT", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime regDt;

    @Column(name = "UPDATE_DT", nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDt;
}
