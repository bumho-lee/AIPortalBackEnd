package esea.esea_api.translation.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Schema(description = "번역 file DTO")
public class TranslateFileRequestDTO {
    @JsonProperty("file")
    private MultipartFile  file; // Base64 인코딩된 파일 데이터

    @JsonProperty("targetLang")
    private String targetLang;

    // Getters and Setters
}
