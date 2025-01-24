package esea.esea_api.admin.role.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ROLE_MANAGER")
@Getter
@Setter
public class RoleManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_MANAGER_ID")
    private Integer roleManagerId; // ROLE_MANAGER_ID (Primary Key)

    @Column(name = "ROLE_ID", nullable = false)
    private Integer roleId; // ROLE_ID

    @Column(name = "USER_ID", nullable = false)
    private String userId; // MANAGER_ID
    
    @Column(name = "USER_NM", nullable = false)
    private String userNm;
    
    @Column(name = "DEPT_ID", nullable = false)
    private String deptId;
    
    @Column(name = "DEPT_NM", nullable = false)
    private String deptNm;

    @Column(name = "TYPE", nullable = false)
    private String type; // TYPE

    @Column(name = "REG_ID", nullable = false)
    private String regId; 
    
    @Column(name = "REG_DT", nullable = false, updatable = false)  // REG_DT 필드 (생성일)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime regDt = LocalDateTime.now();  // 기본값은 현재 시간

    @Column(name = "UPDATE_DT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime updateDt;

}
