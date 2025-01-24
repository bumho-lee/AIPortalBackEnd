package esea.esea_api.gap.dto;

import java.util.List;

import lombok.Data;

@Data
public class LawGapListResponseDto {
    private List<LawGapResponseDTO> content;

    private int totalCount;
}
