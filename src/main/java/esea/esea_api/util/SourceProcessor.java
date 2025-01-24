package esea.esea_api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import esea.esea_api.entities.CollectionData;
import esea.esea_api.repositories.CollectionDataRepository;

import esea.esea_api.chat.dto.SourceResponseDto;

import java.util.ArrayList;
@Component
public class SourceProcessor {
    @Autowired
    private SourcePath sourcePath;

    private final CollectionDataRepository collectionDataRepository;

    public SourceProcessor(CollectionDataRepository collectionDataRepository, 
                           @Value("${some.string.value:defaultValue}") String someString) {
        this.collectionDataRepository = collectionDataRepository;
    }

    // 패턴 분석
    public List<SourceResponseDto> analyzePattern(List<String> sources, String sourceType) {
        List<SourceResponseDto> sourceResponseDtos = new ArrayList<>();

        // 소스 카테고리 분석
        for(String source : sources) {
            // 분석 결과에 '/'가 포함되어 있는 경우 파일
            if(source.contains("/")) {
                sourceResponseDtos.addAll(analyzeFile(source.trim(), sourceType));
            } else {
                sourceResponseDtos.addAll(analyzeName(source.trim(), sourceType));
            }
        }

        return sourceResponseDtos;
    }

    // 파일 분석
    private List<SourceResponseDto> analyzeFile(String source, String sourceType) {
        List<CollectionData> collectionDatas = collectionDataRepository.findByIndexFilePathContainingIgnoreCase(source);

        List<SourceResponseDto> sourceResponseDtos = new ArrayList<>();
        collectionDatas.stream().forEach(collectionData -> {
            SourceResponseDto sourceResponseDto = new SourceResponseDto(collectionData, sourceType, sourcePath);

            // 사규 번호 검색
            if(collectionData.getIndexFilePath().contains("i-portal")) {
                String documentNumber = "";
                String pattern = "[A-Z]-\\d+(?:-\\d+)*"; 
                Pattern r = java.util.regex.Pattern.compile(pattern);
                Matcher m = r.matcher(source);

                if(m.find()) {
                    documentNumber = m.group();
                }
                
                String documentTitle = documentNumber + " " + collectionData.getName();
                sourceResponseDto.setTitle(documentTitle.trim());
            }

            sourceResponseDtos.add(sourceResponseDto);
        });

        return sourceResponseDtos;
    }

    // 일반 이름 분석
    private List<SourceResponseDto> analyzeName(String source, String sourceType) {
        List<SourceResponseDto> sourceResponseDtos = new ArrayList<>();

        CollectionData collectionData = collectionDataRepository.findByName(source);
        
        if (collectionData != null) {
            SourceResponseDto sourceResponseDto = new SourceResponseDto(collectionData, sourceType, sourcePath);
            sourceResponseDtos.add(sourceResponseDto);
        }

        return sourceResponseDtos;
    }

    // 정적 메서드 추가
    public static List<String> processSourceString(String sourceString) {
        String cleanedString = sourceString.replaceAll("[\\[\\]'\"]", "");
        return Arrays.asList(cleanedString.split(","));
    }
}
