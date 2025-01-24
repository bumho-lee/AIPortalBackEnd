package esea.esea_api.chat.dto;

import esea.esea_api.entities.ConversationReaction;
import esea.esea_api.enums.CONVERSATION_CATEGORY;
import esea.esea_api.enums.CONVERSATION_REACTION_TYPE;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class ConversationReactionDto {
    @Schema(description = "반응 유형")
    private CONVERSATION_REACTION_TYPE type;
    @Schema(description = "반응 카테고리")
    private CONVERSATION_CATEGORY category;
    @Schema(description = "반응 이유")
    private String reason;

    public ConversationReactionDto(ConversationReaction reaction) {
        if (reaction != null) {
            this.type = reaction.getType();
            this.category = reaction.getCategory();
            this.reason = reaction.getReason();
        }
    }
}