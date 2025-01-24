package esea.esea_api.statistics.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "대화 로그 응답 DTO")
@Data
public class ConversationLogResponseDto {
    @Schema(description = "대화 로그 목록")
    private List<ConversationLogDto> content;

    private int totalCount;
}
