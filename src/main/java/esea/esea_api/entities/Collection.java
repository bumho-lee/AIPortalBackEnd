package esea.esea_api.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import esea.esea_api.enums.COLLECTION_TYPE;

@Entity
@Data
@Table(name = "COLLECTION")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COLLECTION_ID", nullable = false)
    private Integer collectionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE" , nullable = false, columnDefinition = "COLLECTION_TYPE")
    private COLLECTION_TYPE type;

    @Column(name = "SYSTEM", nullable = true)
    private String system;

    @Column(name = "NAME", nullable = true)
    private String name;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "FOLDER_PATH", nullable = true)
    private String folderPath;

    @Column(name = "USER_ID", nullable = false)
    private String userId;

    @Column(name = "METHOD", nullable = true)
    private String method;

    @CreationTimestamp
    @Column(name = "REG_DT", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime regDt;

    @UpdateTimestamp
    @Column(name = "UPDATE_DT", nullable = true, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime updateDt;

    @Column(name = "INDEX_CODE", nullable = true)
    private String indexCode;

    @Column(name = "USE_YN", nullable = false)
    private String useYN = "N";

    @Column(name = "SOURCE_TYPE", nullable = true)
    private String sourceType;
}
