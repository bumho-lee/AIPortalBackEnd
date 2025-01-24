package esea.esea_api.chat.dto;

import esea.esea_api.entities.Knowledge;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KnowledgeDto {
    private Integer knowledgeId;
    private String category;
    private Boolean active;

    public KnowledgeDto(Knowledge knowledge) {
        this.knowledgeId = knowledge.getKnowledgeId();
        this.category = knowledge.getDisplayName();
        this.active = knowledge.getActive();
    }
}
