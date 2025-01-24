package esea.esea_api.translation.entities;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

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
@Table(name = "COMMON_CODE")
@Getter
@Setter
@NoArgsConstructor
public class CommonCode {

    @Id
    @Column(name = "COMMON_CODE_ID")
    private int commonCodeId;

    @Column(name = "CODE_TYPE")
    private String codeType;

    @Column(name = "COM_CODE")
    private String comCode;

    @Column(name = "COM_CODE_NM")
    private String comCodeNm;
    
}
