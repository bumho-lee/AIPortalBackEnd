package esea.esea_api.admin.login.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.Transient;

@Entity
@Table(name = "USER_LOGIN_HISTORY")
@Getter
@Setter
@ToString
public class UserLoginHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private int id;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "USER_NM", nullable = false)
    private String userNm;

    @Column(name = "IP_ADDR")
    private String ipAddr;

    @Column(name = "LOGIN_YN")
    private String loginYn;

    @Column(name = "LOGIN_TIME")
    private LocalDateTime loginTime;

    @Column(name = "LOGOUT_YN")
    private String logoutYn;

    @Column(name = "LOGOUT_TIME")
    private LocalDateTime logoutTime;

    @Column(name = "DEPT_NM")
    private String deptNm;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Transient
    private String formattedLoginTime;

    public String getFormattedLoginTime() {
        return formattedLoginTime;
    }

    public void setFormattedLoginTime(String formattedLoginTime) {
        this.formattedLoginTime = formattedLoginTime;
    }

}
