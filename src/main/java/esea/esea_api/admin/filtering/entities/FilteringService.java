package esea.esea_api.admin.filtering.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "FILTERING_SERVICE") // 데이터베이스 테이블명
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilteringService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment 설정
    @Column(name = "FILTERING_SERVICE_ID")
    private int filteringServiceId;

    @Column(name = "KEYWORD", nullable = false)
    private String keyword;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "USE_YN", nullable = false)
    private String useYn;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @CreatedDate
    @Column(name = "REG_DT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime regDt;

    @LastModifiedDate
    @Column(name = "UPDATE_DT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDt;
}
