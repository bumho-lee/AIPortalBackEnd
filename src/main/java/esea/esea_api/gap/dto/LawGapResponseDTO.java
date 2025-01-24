package esea.esea_api.gap.dto;

import esea.esea_api.chat.dto.SourceResponseDto;

import esea.esea_api.gap.projections.LawGapProjection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
@Setter
@Schema(description = "갭분석 목록 응답 DTO")
public class LawGapResponseDTO {
    @Schema(description = "갭분석 아이디")
    private Long lawGapId;

    @Schema(description = "갭분석 제목", example = "갭분석 제목")
    private String title = "";

    @Schema(description = "제개정일", example = "2024-01-01")
    private String regDt = "";

    @Schema(description = "시행일", example = "[2024-01-01, 2024-01-02]")
    private List<String> effDts;

    @Schema(description = "개정문", example = "개정문")
    private String amendment = "";

    @Schema(description = "개정이유", example = "개정사유")
    private String amendmentReason = "";

    @Schema(description = "분석결과", example = "분석결과")
    private String analysisResult;

    @Schema(description = "구분", example = "환경")
    private String category = "";

    @Schema(description = "연관 사규", example = "사규")
    private List<SourceResponseDto> regulations;

    @Schema(description = "공포번호 (공동 표기)", example = "")
    private String promulgationName;

    // 피드백 유저 표시
    @Schema(description = "피드백 수", example = "10")
    private Integer feedbackCount;

    @Schema(description = "피드백 내용", example = "피드백 내용")
    private String feedback;

    @Schema(description = "피드백 유저 아이디 목록", example = "[\"user1\", \"user2\"]")
    private List<String> feedbackUsers;
    
    @Schema(description = "담당자 유저 목록", example = "[\"user1\", \"user2\"]")
    private List<LawGapUserInfoDto> managers;

    @Schema(description = "검토자 유저", example = "[\"user1\", \"user2\"]")
    private LawGapUserInfoDto reviewer;

    public LawGapResponseDTO(LawGapProjection lawGap) {
        this.lawGapId = lawGap.getLawGapId();
        this.title =  "(" + lawGap.getLawType() + ") "  + lawGap.getTitle();
        this.amendmentReason = lawGap.getAmendmentReason();
        
        // 이미지 태그 제거
        String amendmentText = lawGap.getParsedArticlesText() != null ? lawGap.getParsedArticlesText() : "";
        this.amendment = amendmentText.replaceAll("<img[^>]*>|</img>", "").trim();

        // 분석 결과 설정
        if(lawGap.getAnalysisResult() != null && !lawGap.getAnalysisResult().isEmpty()) {
            try {
                JsonNode analysisResult = new ObjectMapper().readTree(lawGap.getAnalysisResult());
                
                JsonNode gapAnalysisSummary = analysisResult.get("gap_analysis_summary");
                this.analysisResult = gapAnalysisSummary != null ? gapAnalysisSummary.asText() : "";
                this.analysisResult = this.analysisResult.replaceAll("^\\s+|\\s+$", "");
                
            } catch (JsonProcessingException e) {
                this.analysisResult = "";
                e.printStackTrace();
            }
        }

        // 별표 처리
        processArticleContent(lawGap.getStarredArticles(), "tables");

        // 서식 처리
        processArticleContent(lawGap.getStarredArticles(), "forms");

        // 행정규칙 처리
        if(lawGap.getRuleData() != null && !lawGap.getRuleData().isEmpty()) {
            this.amendment += "\n\n[행정규칙 목록]";

            try {
                JsonNode ruleData = new ObjectMapper().readTree(lawGap.getRuleData());
                
                for(JsonNode rule : ruleData) {
                    this.amendment += "\n" + rule.path("행정규칙명").asText();
                }
                
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        // 피드백 수
        this.feedbackCount = lawGap.getFeedbackCount();

        // 피드백 내용
        String check = lawGap.getFeedbackCount() > 0 ? "확인" : "미확인";
        this.feedback = check + " (" + lawGap.getFeedbackCount() + ")";

        // 제개정일
        this.regDt = lawGap.getRegDt()
            .atZone(ZoneId.of("Asia/Seoul"))
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        // 시행일 포맷 변경
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        
        this.effDts = Arrays.stream(lawGap.getEffDts().split(","))
            .map(String::trim)
            .map(date -> LocalDateTime.parse(date, inputFormatter)
                .atZone(ZoneId.of("Asia/Seoul"))
                .format(outputFormatter))
            .collect(Collectors.toList());

        this.promulgationName = lawGap.getPromulgationName();
            
        processManagers(lawGap.getManagers());
        processReviewer(lawGap.getReviewer());
    }

    // 내부 함수
    private void appendContentWithDate(StringBuilder builder, String content, String date) {
        if (content != null && !content.trim().isEmpty()) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(content.trim());
            if (date != null && !date.isEmpty()) {
                builder.append(" (").append(date).append(")");
            }
        }
    }

    // 내부 함수
    private void processArticleContent(String starredArticles, String type) {
        if (starredArticles == null || starredArticles.isEmpty()) {
            return;
        }

        try {
            JsonNode articlesArray = new ObjectMapper().readTree(starredArticles);
            StringBuilder contentBuilder = new StringBuilder();
            
            for (JsonNode article : articlesArray) {
                String date = article.path("date").asText("");
                JsonNode items = article.get(type); // "tables" 또는 "forms"
                
                if (items != null && items.isArray()) {
                    for (JsonNode item : items) {
                        appendContentWithDate(contentBuilder, item.asText(), date);
                    }
                }
            }
            
            String content = contentBuilder.toString().trim();
            if (!content.isEmpty()) {
                this.amendment += String.format("\n\n[%s 목록]\n%s", 
                    type.equals("tables") ? "별표" : "서식", 
                    content);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void processManagers(String managers) {
        if(managers != null && !managers.isEmpty()) {
            try {
                JsonNode managersJson = new ObjectMapper().readTree(managers);
                List<LawGapUserInfoDto> managerList = new ArrayList<>();

                for(JsonNode manager : managersJson) {
                    String userName = manager.path("userName").asText();
                    String deptName = manager.path("deptName").asText();
                    String userId = manager.path("userId").asText();
                    String deptId = manager.path("deptId").asText();

                    LawGapUserInfoDto managerDto = new LawGapUserInfoDto();
                    managerDto.setUserName(userName);
                    managerDto.setDeptName(deptName);
                    managerDto.setUserId(userId);
                    managerDto.setDeptId(deptId);

                    managerList.add(managerDto);
                }

                this.managers = managerList;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private void processReviewer(String reviewer) {
        if(reviewer != null && !reviewer.isEmpty()) {
            try {
                JsonNode reviewerJson = new ObjectMapper().readTree(reviewer);
                
                String userName = reviewerJson.path("userName").asText();
                String deptName = reviewerJson.path("deptName").asText();
                String userId = reviewerJson.path("userId").asText();
                String deptId = reviewerJson.path("deptId").asText();

                this.reviewer = new LawGapUserInfoDto();
                this.reviewer.setUserName(userName);
                this.reviewer.setDeptName(deptName);
                this.reviewer.setUserId(userId);
                this.reviewer.setDeptId(deptId);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
