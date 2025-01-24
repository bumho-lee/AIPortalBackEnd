package esea.esea_api.gap.dto.law_gap_detail;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Transient;

@Schema(description = "행정규칙", example = "")
@Data
@AllArgsConstructor
public class RuleResponseDto {
    @Schema(description = "법종 구분", example = "")
    private String kind;

    @Schema(description = "행정규칙 명", example = "")
    private String title;

    @Schema(description = "본문상세링크", example = "")
    private String url;

    @Schema(description = "행정규칙 재/개정 링크", example = "")
    private String revisionUrl;

    @Transient
    @JsonIgnore
    private Date regDate;

    public RuleResponseDto(JsonNode rule) {
        this.kind = rule.path("법종구분").asText();
        String title = rule.path("행정규칙명").asText();
        this.url = "https://www.law.go.kr/admRulLsInfoP.do?admRulSeq=" + _ExtractId(rule.path("본문상세링크").asText());
        this.revisionUrl = "https://www.law.go.kr/admRulInfoP.do?urlMode=admRulRvsInfoR&admRulSeq=" + _ExtractId(rule.path("본문상세링크").asText());

        String effDt = rule.path("시행일자").asText();
        String regDt = rule.path("발령일자").asText();
        String dept = rule.path("소관부처명").asText();
        String effNum = rule.path("발령번호").asText();

        // YYYYMMDD -> YYYY.MM.DD 형식으로 변환
        if (effDt != null && effDt.length() == 8) {
            effDt = LocalDate.parse(effDt, DateTimeFormatter.ofPattern("yyyyMMdd"))
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
        
        if (regDt != null && regDt.length() == 8) {
            regDate = java.sql.Date.valueOf(LocalDate.parse(regDt, DateTimeFormatter.ofPattern("yyyyMMdd")));

            regDt = LocalDate.parse(regDt, DateTimeFormatter.ofPattern("yyyyMMdd"))
                    .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));            
        }

        this.title = title + " [시행 " + effDt + "] [" + dept + " 제" + effNum + "호, " + regDt + "]";
    }

    private String _ExtractId(String input) {
        Pattern pattern = Pattern.compile("ID=(\\d+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        }
    
        return null; // ID가 없을 경우 null 반환
    }
}
