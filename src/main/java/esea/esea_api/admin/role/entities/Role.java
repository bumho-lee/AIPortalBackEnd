package esea.esea_api.admin.role.entities;

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

@Setter
@Getter
@Entity
@Table(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private int roleId;

    @Column(name = "NAME")
    private String name;
    
    @Column(name = "USE_YN")
    private String useYn;

    @Column(name = "REG_ID")
    private String regId;

    @Column(name = "REG_DT", nullable = false, updatable = false)  // REG_DT 필드 (생성일)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime regDt = LocalDateTime.now();  // 기본값은 현재 시간

    @Column(name = "UPDATE_DT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDt;
}
