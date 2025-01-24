package esea.esea_api.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import esea.esea_api.enums.COLLECTION_METHOD;

@Entity
@Getter
@Setter
@Table(name = "COLLECTION_DATA")
public class CollectionData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COLLECTION_DATA_ID")
    private Integer collectionDataId;

    @Column(name = "NAME", nullable = false, columnDefinition = "text")
    private String name;

    @Column(name = "FILE_NAME", nullable = true, columnDefinition = "text")
    private String fileName;

    @Column(name = "FILE_PATH", nullable = true, columnDefinition = "text")
    private String filePath;

    @Column(name = "ORIGINAL_FILE_PATH", nullable = true, columnDefinition = "text")
    private String originalFilePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "METHOD", nullable = false)
    private COLLECTION_METHOD method = COLLECTION_METHOD.AUTO;

    @Column(name = "USER_ID", nullable = false, columnDefinition = "text")
    private String userId;

    @CreationTimestamp
    @Column(name = "REG_DT", nullable = false)
    private OffsetDateTime regDt;

    @UpdateTimestamp
    @Column(name = "UPDATE_DT", nullable = false)
    private OffsetDateTime updateDt;

    @Column(name = "INDEX_UPDATE", nullable = false, columnDefinition = "boolean default false")
    private Boolean indexUpdate = false;

    @Column(name = "INDEX_FILE_PATH", nullable = true, columnDefinition = "text")
    private String indexFilePath;

    @Column(name = "COLLECTION_ID", nullable = false)
    private Integer collectionId;
}
