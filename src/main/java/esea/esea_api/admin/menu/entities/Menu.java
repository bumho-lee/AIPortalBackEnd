package esea.esea_api.admin.menu.entities;

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
@Table(name = "MENUS")
public class Menu{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID 필드는 자동 증가
    @Column(name = "ID")
    private int id;  // 기본 키 필드 (자동 증가)

    @Column(name = "MENU_ID", nullable = false)  // MENU_ID 필드는 nullable(false)로 설정
    private String menu_id;
 
    @Column(name = "MENU_NAME", nullable = false)  // MENU_NAME 필드
    private String menu_name;

    @Column(name = "MENU_ORDER")  // MENU_ORDER 필드
    private Integer menuOrder = 0;

    @Column(name = "PARENT_ID")  // PARENT_ID 필드
    private String parentId = "0";

    @Column(name = "LEVEL")  // LEVEL 필드
    private Integer level = 0;

    @Column(name = "PATH")  // PATH 필드
    private String path;
    
    @Column(name = "ICON")  // ICON 필드
    private String icon;
    
    @Column(name = "VIEW_YN")  // VIEW_YN 필드
    private String viewYn;

    @Column(name = "USE_YN")  // USEYN 필드
    private String useYn;
    
    @Column(name = "REG_DT", nullable = false, updatable = false)  // REG_DT 필드 (생성일)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime regDt = LocalDateTime.now();  // 기본값은 현재 시간
}