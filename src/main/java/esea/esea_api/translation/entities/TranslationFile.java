package esea.esea_api.translation.entities;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TRANSLATION_FILES")
@Getter
@Setter
@NoArgsConstructor
public class TranslationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILE_ID")
    private int file_id; // 고유 식별자

    @Column(name = "ORIGINAL_FILENAME")
    private String originalFilename; // 번역 파일명

    @Column(name = "ORIGINAL_FILE_URL")
    private String originalFileUrl; // 번역 파일 다운로드 URL

    @Column(name = "TRANSLATED_FILENAME")
    private String translatedFilename; // 번역 완료 파일명

    @Column(name = "TRANSLATED_FILE_URL")
    private String translatedFileUrl; // 번역 완료 파일 다운로드 URL
    
    @Column(name = "UP_FILE_URL")
    private String upFileUrl; 
    
    @Column(name = "DOWN_FILE_URL")
    private String downFileUrl; 

    @Column(name = "LANGUAGE")
    private String language; // 언어
    
    @Column(name = "SORCELANG")
    private String sorcelang; // 언어
    
    @Column(name = "TRANSLTED_CHARCNT")
    private int translted_charcnt; // 파일번역 테스트수

    @Column(name = "REG_ID")
    private String regId; // 등록자 ID

    @Column(name = "REG_NM")
    private String regNm; // 등록자 이름
    
    @Column(name = "DEPT_ID")
    private String deptId; // 부서

    @Column(name = "DEPT_NM")
    private String deptNm; // 부서 이름

    @Column(name = "REG_DT")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDt; // 업로드한 시간

    @Column(name = "COMPLETION_TIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completionTime; // 완료된 시간

}
