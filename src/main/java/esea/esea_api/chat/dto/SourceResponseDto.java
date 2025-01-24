package esea.esea_api.chat.dto;

import lombok.Getter;
import lombok.Setter;

import esea.esea_api.enums.CONVERSATION_SOURCE_TYPE;
import esea.esea_api.util.SourcePath;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.beans.factory.annotation.Autowired;

import esea.esea_api.entities.CollectionData;

@Getter
@Setter
@Schema(description = "대화 소스 응답 DTO")
public class SourceResponseDto {
    private final SourcePath sourcePath;

    @Schema(description = "소스 URL 또는 텍스트", example = "https://example.com/document")
    private String source;

    @Schema(description = "소스 제목", example = "참고 문서")
    private String title;

    @Schema(description = "소스 타입", example = "URL")
    private CONVERSATION_SOURCE_TYPE type;

    @Schema(description = "소스 유니크 번호", example = "1")
    private int collectionDataId;

    @Schema(description = "지식 아이디", example = "1")
    private int knowledgeId;

    public SourceResponseDto(CollectionData collectionData, String sourceType, SourcePath sourcePath) {
        this.sourcePath = sourcePath;
        this.collectionDataId = collectionData.getCollectionDataId();

        // 이름 설정
        this.title = collectionData.getName();

        // index 파일인 경우
        if (sourceType.equals("INDEX_FILE_PATH")) {
            setSourceForIndexFile(collectionData);

            // 사규인 경우
            if(collectionData.getIndexFilePath().contains("i-portal")) {
                String[] pathParts = collectionData.getIndexFilePath().split("/");
                this.title = pathParts[pathParts.length - 1];
            }
        } else {
            setSourceBasedOnFileType(collectionData);
        }
    }

    // index 파일인 경우
    private void setSourceForIndexFile(CollectionData collectionData) {
        String filePath = collectionData.getIndexFilePath();
        String normalizedPath = normalizeFilePath(filePath);

        this.source = sourcePath.buildS3Url(normalizedPath);
        this.type = CONVERSATION_SOURCE_TYPE.PDF;
    }

    // index 파일이 아닌 경우
    private void setSourceBasedOnFileType(CollectionData collectionData) {
        String filePath = collectionData.getFilePath();
        
        if (filePath.contains("law")) {
            // 법제처 데이터 처리
            this.source = collectionData.getOriginalFilePath();
            this.type = CONVERSATION_SOURCE_TYPE.URL;
            return;
        }
        
        if (filePath.contains("kgs-code") || filePath.contains("spec")) {
            // KGS 코드 및 스펙 데이터 처리
            String normalizedPath = normalizeFilePath(filePath);
            this.source = sourcePath.buildS3Url(normalizedPath);
            this.type = determineFileType(this.source);
        }
    }

    // 파일 경로 정규화
    private String normalizeFilePath(String filePath) {
        return !filePath.startsWith("/") ? "/" + filePath : filePath;
    }

    // 파일 유형 결정
    private CONVERSATION_SOURCE_TYPE determineFileType(String source) {
        return source.endsWith(".pdf") ? CONVERSATION_SOURCE_TYPE.PDF : CONVERSATION_SOURCE_TYPE.FILE;
    }
}
