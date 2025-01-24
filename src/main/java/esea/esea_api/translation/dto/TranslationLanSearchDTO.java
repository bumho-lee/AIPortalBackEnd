package esea.esea_api.translation.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "번역 LAN DTO")
public class TranslationLanSearchDTO {
    
    @Schema(description = "코드타입", example = "LAN", required = false, nullable = true)
    private String codeType;
}