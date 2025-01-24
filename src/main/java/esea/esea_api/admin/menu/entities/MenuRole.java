package esea.esea_api.admin.menu.entities;

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
@Table(name = "MENU_ROLE")
public class MenuRole{
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ROLE_ID")
	private int menuRoleId;

    @Column(name = "MENU_ID")
    private String menuId;

    @Column(name = "ROLE_ID")
    private int roleId;
}