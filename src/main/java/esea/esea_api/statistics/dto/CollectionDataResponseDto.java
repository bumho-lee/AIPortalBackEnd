package esea.esea_api.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import esea.esea_api.entities.CollectionData;
import lombok.Data;

@Schema(description = "수집 Data 응답 DTO")
@Data
public class CollectionDataResponseDto {
    @Schema(description = "수집 Data ID")
    private String collectionDataId;

    @Schema(description = "수집 Data 제목")
    private String title;

    @Schema(description = "수집 Data 파일명")
    private String fileName;

    @Schema(description = "등록일")
    private String regDt;

    @Schema(description = "시행일")
    private String activeDt;

    @Schema(description = "파일 패스")
    private String filePath;

    @Schema(description = "처리 여부")
    private String processStatus;

    @Schema(description = "rag 처리 여부")
    private String ragYn;
    
    public CollectionDataResponseDto(CollectionData collectionData) {
        this.collectionDataId = collectionData.getCollectionDataId().toString();
        this.title = collectionData.getName();
        this.fileName = collectionData.getFileName();
        this.filePath = collectionData.getFilePath() != null ? 
            (collectionData.getFilePath().startsWith("/") ? collectionData.getFilePath().substring(1) : collectionData.getFilePath())
            : null;
        this.filePath = "https://htc-ai-datalake.s3.ap-northeast-2.amazonaws.com/" + this.filePath;

        this.regDt = collectionData.getRegDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.ragYn = collectionData.getIndexUpdate() ? "Y" : "N";
        
        if(collectionData.getUpdateDt() != null) {
            this.activeDt = collectionData.getUpdateDt()
                .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } else {
            this.activeDt = "";
        }

        if(collectionData.getRegDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            .equals(collectionData.getUpdateDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))) {
            this.processStatus = "생성";
        } else {
            this.processStatus = "수정";
        }
    }
}
