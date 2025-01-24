package esea.esea_api.entities;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "KNOWLEDGE")
@Getter
@Setter
@NoArgsConstructor
public class Knowledge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KNOWLEDGE_ID")
    private Integer knowledgeId;

    @Column(name = "DISPLAY_NAME", nullable = false)
    private String displayName;

    @Column(name = "INDEX_CODE", nullable = true)
    private List<String> indexCode;

    @Column(name = "ACTIVE", nullable = false, columnDefinition = "boolean default false")
    private Boolean active = false;

    @Column(name = "REG_DT", nullable = false)
    private OffsetDateTime regDt;
}
