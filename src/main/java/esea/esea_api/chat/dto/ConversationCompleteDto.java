package esea.esea_api.chat.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "대화 완료 응답 DTO")
public class ConversationCompleteDto {
    @Schema(description = "전문가 응답 내용", example = "전문가의 상세한 답변입니다...")
    private String expert;
    @Schema(description = "참고한 소스 목록")
    private List<SourceResponseDto> sources;

    public ConversationCompleteDto(String expert, List<SourceResponseDto> sources) {
        this.expert = expert;
        this.sources = sources;
    }
}
