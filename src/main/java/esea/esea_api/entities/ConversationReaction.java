package esea.esea_api.entities;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;

import esea.esea_api.enums.CONVERSATION_CATEGORY;
import esea.esea_api.enums.CONVERSATION_REACTION_TYPE;

@Entity
@Table(name = "CONVERSATION_REACTION")
public class ConversationReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONVERSATION_REACTION_ID", nullable = false)
    private Integer conversationReactionId;

    @Column(name = "CONVERSATION_ID", nullable = false)
    private String conversationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private CONVERSATION_REACTION_TYPE type;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY", nullable = false)
    private CONVERSATION_CATEGORY category;

    @Column(name = "REASON")
    private String reason;

    @CreationTimestamp
    @Column(name = "REG_DT", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime regDt;

    // Getter 메서드
    public Integer getConversationReactionId() {
        return conversationReactionId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public CONVERSATION_REACTION_TYPE getType() {
        return type;
    }

    public CONVERSATION_CATEGORY getCategory() {
        return category;
    }

    public String getReason() {
        return reason;
    }

    public OffsetDateTime getRegDt() {
        return regDt;
    }

    // Setter 메서드
    public void setConversationReactionId(Integer conversationReactionId) {
        this.conversationReactionId = conversationReactionId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setType(CONVERSATION_REACTION_TYPE type) {
        this.type = type;
    }

    public void setCategory(CONVERSATION_CATEGORY category) {
        this.category = category;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setRegDt(OffsetDateTime regDt) {
        this.regDt = regDt;
    }
}
