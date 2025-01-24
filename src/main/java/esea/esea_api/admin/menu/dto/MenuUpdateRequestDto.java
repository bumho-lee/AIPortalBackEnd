package esea.esea_api.admin.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
@Schema(description = "메뉴 수정 요청 DTO")
public class MenuUpdateRequestDto {
    @Schema(description = "메뉴 ID")
    int id;
    @Schema(description = "부모 ID")
    String parentId;
    @Schema(description = "메뉴 순서")
    int menuOrder;
    @Schema(description = "메뉴 ID")
    String menu_id;
    @Schema(description = "메뉴 이름")
    String menu_name;
    @Schema(description = "메뉴 보기 여부")
    String viewYn;
    @Schema(description = "메뉴 사용 여부")
    String useYn;
    @Schema(description = "메뉴 경로")
    String path;
}