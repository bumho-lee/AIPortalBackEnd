package esea.esea_api.entities;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CONVERSATION_LOG")
@Data
public class ConversationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONVERSATION_LOG_ID")
    private Integer conversationLogId;

    @Column(name = "CONVERSATION_ID")
    private String conversationId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "QUERY")
    private String query;

    @Column(name = "ANSWER")
    private String answer;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "REG_DT")
    private OffsetDateTime regDt;

    @Column(name = "LLM_MODEL")
    private String llmModel;

    @Column(name = "CATEGORY")
    private List<String> category;
}
