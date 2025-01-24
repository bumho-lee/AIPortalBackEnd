package esea.esea_api.translation.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteTranslationManager {
	private List<Integer> ids; // 삭제할 ID 목록
}
