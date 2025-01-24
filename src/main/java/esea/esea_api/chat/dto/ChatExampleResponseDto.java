package esea.esea_api.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import esea.esea_api.entities.ChatExample;

@Getter
@NoArgsConstructor
public class ChatExampleResponseDto {
    private String header;
    private String content;
    private Integer knowledgeId;

    @Builder
    public ChatExampleResponseDto(ChatExample entity) {
        System.out.println(entity.getHeader());

        this.header = entity.getHeader();
        this.content = entity.getContent();
        this.knowledgeId = entity.getKnowledgeId();
    }
}
