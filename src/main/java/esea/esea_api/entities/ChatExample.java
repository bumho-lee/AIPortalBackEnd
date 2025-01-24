package esea.esea_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "CHAT_EXAMPLE")
@Getter
@Setter
@NoArgsConstructor
public class ChatExample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_EXAMPLE_ID")
    private Integer chatExampleId;

    @Column(name = "HEADER", nullable = false)
    private String header;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "REG_DT", nullable = false)
    private OffsetDateTime regDt;

    @Column(name = "KNOWLEDGE_ID", nullable = true)
    private Integer knowledgeId;
}
