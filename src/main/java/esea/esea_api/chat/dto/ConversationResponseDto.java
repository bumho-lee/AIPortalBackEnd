package esea.esea_api.chat.dto;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class ConversationResponseDto {
    @Schema(description = "대화 아이디")
    private String conversationId;

    @Schema(description = "채팅 아이디")
    private String chatId;

    @Schema(description = "등록일")
    private String regDt;

    @Schema(description = "질문")
    private String question = "";

    @Schema(description = "답변")
    private String answer = "";

    @Schema(description = "대화 반응")
    private ConversationReactionDto conversationReaction;

    @Schema(description = "전문가")
    private String expert = StringUtils.defaultIfEmpty(System.getenv("EXPERT_LINK"), "https://she.hanwha-total.net/SafetyQnA/Inquiry/Index");

    @Schema(description = "출처")
    private List<SourceResponseDto> sources = new ArrayList<>();

    @Schema(description = "지식 정보")
    private List<Integer> knowledgeIds = new ArrayList<>();

    public ConversationResponseDto(Map<String, Object> conversation, String sk) {
        this.chatId = sk;
        this.conversationId = (String) conversation.get("message_id");
        this.regDt = (String) conversation.get("qa_create_time");
        this.question = (String) conversation.get("query");
        
        // answer에서 출처 부분을 제거하고 실제 답변만 저장
        String fullAnswer = (String) conversation.get("answer");
        this.answer = fullAnswer.split("🔥 \\*\\*출처\\*\\*")[0].trim();

        // 이미 출처 정보를 전처리 한 경우
        @SuppressWarnings("unchecked")
        List<SourceResponseDto> sourcesList = (List<SourceResponseDto>) conversation.get("sources");
        this.sources = sourcesList != null ? sourcesList : new ArrayList<>();

        @SuppressWarnings("unchecked")
        List<Integer> knowledgeIdsList = (List<Integer>) conversation.get("knowledge");
        this.knowledgeIds = knowledgeIdsList != null ? knowledgeIdsList : new ArrayList<>();
    }
}