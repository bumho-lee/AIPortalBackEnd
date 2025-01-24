package esea.esea_api.statistics.dto;

import lombok.Data;

import esea.esea_api.enums.COLLECTION_TYPE;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "수집 관리 요청 DTO")
@Data
public class CollectionManagementRequestDto {    
    @Schema(description = "사용자 ID")
    private String userId;

    @Schema(description = "사내/사외")
    private COLLECTION_TYPE type;

    @Schema(description = "시스템 명 (예 I-PORTAL)")
    private String system;

    @Schema(description = "문서 (예: 안전환경커뮤니티)")
    private String name;

    @Schema(description = "S3 파일 경로")
    private String s3Path;

    @Schema(description = "인터페이스 (AUTO | MANUAL)")
    private String method;

    @Schema(description = "배치 정상 여부 (Y | N)")
    private String status;

    @Schema(description = "사용 여부 (Y | N)")
    private String useYn;
}
