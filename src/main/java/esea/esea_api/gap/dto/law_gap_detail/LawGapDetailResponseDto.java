package esea.esea_api.gap.dto.law_gap_detail;

import esea.esea_api.chat.dto.ConversationResponseDto;
import esea.esea_api.chat.dto.SourceResponseDto;

import java.time.ZoneId;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import esea.esea_api.entities.LawGap;
import esea.esea_api.gap.dto.LawGapUserInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "갭분석 상세정보")
@Data
public class LawGapDetailResponseDto {
    @Schema(description = "갭분석 아이디", example = "1")
    private Integer lawGapId;

    @Schema(description = "법령명", example = "법령명")
    private String title;

    @Schema(description = "개정일", example = "2024-01-01")
    private String regDt;

    @Schema(description = "시행일", example = "[2024-01-01, 2024-01-02]")
    private List<String> effDts;

    @Schema(description = "변경 내용", example = "")
    private String amendment;

    @Schema(description = "법령관련 링크", example = "")
    private List<LawGapLinkDto> links;

    @Schema(description = "신구법 비교 링크", example = "")
    private LawGapLinkDto newLawComparison;

    @Schema(description = "분석 결과", example = "")
    private String analysisResult;

    @Schema(description = "좋아요/싫어요 상태", example = "true")
    private Boolean likeDislikeStatus;

    @Schema(description = "피드백 상태", example = "true")
    private String feedbackYn;

    @Schema(description = "채팅 내용", example = "")
    private List<ConversationResponseDto> content;

    @Schema(description = "연관 사규", example = "")
    private List<SourceResponseDto> regulations;

    @Schema(description = "행정규칙", example = "")
    private List<RuleResponseDto> rules;

    @Schema(description = "공포번호 (공동 표기)", example = "")
    private String promulgationName;

    @Schema(description = "법종구분", example = "")
    private String lawType;

    @Schema(description = "법률 이름", example = "")
    private String topLawName;

    @Schema(description = "제개정 이유", example = "")
    private String amendmentReason;

    @Schema(description = "제개정 이유 요약", example = "")
    private String lawSummary;

    // 담당자 목록
    @Schema(description = "담당자 목록", example = "")
    private List<LawGapUserInfoDto> managers;

    // 검토자
    @Schema(description = "검토자 목록", example = "")
    private LawGapUserInfoDto reviewer;

    public LawGapDetailResponseDto(LawGap gap) {
        this.lawGapId = gap.getLawGapId();
        this.title = gap.getTitle();
        this.promulgationName = gap.getPromulgationName();
        this.lawType = gap.getLawType();
        this.topLawName = gap.getTopLawName();
        this.amendmentReason = gap.getAmendmentReason();
        
        this.regDt = gap.getRegDt()
            .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
            .format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        // 시행일
        this.effDts = gap.getEffDts().stream()
            .map(date -> date.atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy.MM.dd")))
            .toList();
        
        // 변경내용
        String amendmentText = gap.getParsedArticlesText() != null ? gap.getParsedArticlesText() : "";
        
        // <img 태그가 있는 경우에만 처리
        if (amendmentText.contains("<img")) {
            // <img 태그가 시작되는 위치 찾기
            int imgStartIndex = amendmentText.indexOf("<img");
            // <img 태그 이전의 내용만 유지
            amendmentText = amendmentText.substring(0, imgStartIndex).trim();
        }

        // 이미지 태그 제거 (혹시 남아있을 수 있는 태그들을 위해)
        this.amendment = amendmentText.replaceAll("<img[^>]*>|</img>", "").trim();

        // 별표 처리
        processArticleContent(gap.getStarredArticles(), "tables");

        // 서식 처리
        processArticleContent(gap.getStarredArticles(), "forms");

        // rules 리스트 초기화 추가
        this.rules = new ArrayList<>();

        // 행정규칙 처리
        if(gap.getRuleData() != null && !gap.getRuleData().isEmpty()) {
            try {
                JsonNode ruleData = new ObjectMapper().readTree(gap.getRuleData());
                
                for(JsonNode rule : ruleData) {
                    RuleResponseDto ruleDto = new RuleResponseDto(rule);
                    this.rules.add(ruleDto);
                }

                this.rules.sort(Comparator.comparing(RuleResponseDto::getRegDate).reversed());
                
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } 

        // 분석 결과 설정
        if(gap.getAnalysisResult() != null && !gap.getAnalysisResult().isEmpty()) {
            try {
                JsonNode analysisResult = new ObjectMapper().readTree(gap.getAnalysisResult());
                
                JsonNode gapAnalysisSummary = analysisResult.get("gap_analysis_summary");
                this.analysisResult = gapAnalysisSummary != null ? gapAnalysisSummary.asText() : "";
                this.analysisResult = this.analysisResult.replaceAll("^\\s+|\\s+$", "");
            } catch (JsonProcessingException e) {
                this.analysisResult = "";
                e.printStackTrace();
            }
        } else {
            this.analysisResult = "";
        }

        // 신구법 비교 링크
        LawGapLinkDto newLawComparisonLink = new LawGapLinkDto();
        newLawComparisonLink.setLink("https://www.law.go.kr/lsInfoP.do?viewCls=lsOldAndNew&lsiSeq=" + gap.getMst());
        newLawComparisonLink.setTitle("신구법 비교");
        this.newLawComparison = newLawComparisonLink;

        // 법령관련 링크
        LawGapLinkDto link1 = new LawGapLinkDto();
        link1.setLink("https://www.law.go.kr/lsStmdInfoP.do?lsiSeq=" + gap.getMst());
        link1.setTitle("법령 체계도");
        this.links = List.of(link1);

        LawGapLinkDto link2 = new LawGapLinkDto();
        link2.setLink("https://www.law.go.kr/lsInfoP.do?viewCls=lsRvsDocInfoR&lsiSeq=" + gap.getMst());
        link2.setTitle("재/개정 이유");
        this.links = List.of(link1, link2);

        // 담당자 목록
        processManagers(gap.getManagers());

        // 검토자
        processReviewer(gap.getReviewer());
    }

    // 내부 함수
    private void appendContentWithDate(StringBuilder builder, String content, String date) {
        if (content != null && !content.trim().isEmpty()) {
            if (builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(content.trim());
            if (date != null && !date.isEmpty()) {
                // YYYYMMDD 형식을 YYYY.MM.DD 형식으로 변환
                if (date.length() == 8) {
                    LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
                    date = parsedDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                }

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
            // 법령 변경 내용 파싱
            JsonNode articlesArray = new ObjectMapper().readTree(starredArticles);
            StringBuilder contentBuilder = new StringBuilder();
            
            for (JsonNode article : articlesArray) {
                String date = article.path("date").asText("");
                
                JsonNode items = article.get(type); // "tables" 또는 "forms"
                
                // 변경 내용이 있는 경우
                if (items != null && items.isArray()) {
                    for (JsonNode item : items) {
                        String pdfLink = item.path("pdf_link").asText("");
                        String starredArticleName = item.path("name").asText("");
                        String number = item.path("number").asText("");
                        String sub_number = item.path("sub_number").asText("");
            
                        String formattedContent = String.format("<a class='law-form-link' href='%s'>[%s%s%s] %s</a>",
                            pdfLink,
                            type.equals("tables") ? "별표" : "서식",
                            number,
                            (!sub_number.isEmpty() && !sub_number.equals("0")) ? "의" + sub_number : "",
                            starredArticleName);
                            
                        appendContentWithDate(contentBuilder, formattedContent, date);
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
