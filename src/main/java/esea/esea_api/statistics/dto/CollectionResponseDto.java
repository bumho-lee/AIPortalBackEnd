package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import esea.esea_api.entities.Collection;
import esea.esea_api.enums.COLLECTION_TYPE;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Schema(description = "컬렉션 응답 DTO")
@Data
public class CollectionResponseDto {
    @Schema(description = "No.")
    private String collectionId;

    @Schema(description = "사내/사외")    
    private String type;

    @Schema(description = "시스템")
    private String system;

    @Schema(description = "Batch Job 이름")
    private String name;

    @Schema(description = "배치 성공 여부")
    private String status;

    @Schema(description = "사용 여부")
    private String use;

    @Schema(description = "시행 일시")
    private String activeDt;
    
    @Schema(description = "등록자 ID")
    private String userId;
    
    @Schema(description = "등록자 이름")
    private String userName;
    
    @Schema(description = "등록일")
    private String regDt;

    public CollectionResponseDto(Collection collection) {
        this.collectionId = collection.getCollectionId().toString();
        this.type = collection.getType() == COLLECTION_TYPE.INTERNAL ? "사내" : "사외";
        this.system = collection.getSystem();
        this.name = collection.getName();
        
        this.status = "COMPLETE".equals(collection.getStatus()) ? "RAG 완료" : "RAG 미완료";
        this.use = "Y".equals(collection.getUseYN()) ? "사용" : "미사용";

        this.userId = collection.getUserId();
        this.regDt = collection.getRegDt()
            .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (collection.getUpdateDt() != null) {
            this.activeDt = collection.getUpdateDt()
                .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            this.activeDt = "";
        }
    }
}
