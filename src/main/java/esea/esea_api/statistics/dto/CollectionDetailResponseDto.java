package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import esea.esea_api.entities.Collection;
import esea.esea_api.enums.COLLECTION_TYPE;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Schema(description = "컬렉션 응답 DTO")
@Data
public class CollectionDetailResponseDto {
    @Schema(description = "No.")
    private String collectionId;

    @Schema(description = "사내/사외")    
    private String type;

    @Schema(description = "시스템")
    private String system;

    @Schema(description = "Batch Job 이름")
    private String name;

    @Schema(description = "배치 정상 여부")
    private String status;

    @Schema(description = "시행 일시")
    private String activeDt;
    
    @Schema(description = "등록자 ID")
    private String userId;
    
    @Schema(description = "등록자 이름")
    private String userName;
    
    @Schema(description = "등록일")
    private String regDt;
    
    @Schema(description = "s3 경로")
    private String s3Path;

    @Schema(description = "인터페이스 (AUTO | MANUAL)")
    private String method;

    @Schema(description = "S3 버킷")
    private String baseUrl = "https://ddi-ai-datalake.s3.ap-northeast-2.amazonaws.com";

    @Schema(description = "사용 여부")
    private String useYn;

    @Schema(description = "log 총 수량")
    private long totalCount;
    
    public CollectionDetailResponseDto(Collection collection) {
        this.collectionId = collection.getCollectionId().toString();
        this.type = collection.getType() == COLLECTION_TYPE.INTERNAL ? "사내" : "사외";
        this.method = collection.getMethod();
        this.system = collection.getSystem();
        this.name = collection.getName();
        this.status = "COMPLETE".equals(collection.getStatus()) ? "Y" : "N";
        this.userId = collection.getUserId();
        this.s3Path = collection.getFolderPath();

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

        this.useYn = collection.getUseYN();
    }
}
