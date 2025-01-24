package esea.esea_api.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import esea.esea_api.repositories.CollectionRepository;
import esea.esea_api.statistics.dto.CollectionManagementRequestDto;
import esea.esea_api.statistics.dto.CollectionResponseDto;
import esea.esea_api.translation.util.S3Util;
import esea.esea_api.statistics.dto.CollectionDataLogResponseDto;
import esea.esea_api.statistics.dto.CollectionDataRequestDto;
import esea.esea_api.statistics.dto.CollectionDataResponseDto;
import esea.esea_api.statistics.dto.CollectionDeleteRequestDto;
import esea.esea_api.statistics.dto.CollectionDetailResponseDto;
import esea.esea_api.statistics.dto.CollectionListRequestDto;
import esea.esea_api.statistics.dto.CollectionListResponseDto;
import esea.esea_api.statistics.dto.CollectionManagementUpdateRequestDto;
import esea.esea_api.statistics.dto.CollectionManagementDetailRequestDto;
import esea.esea_api.repositories.CollectionDataRepository;

import esea.esea_api.entities.Collection;
import esea.esea_api.entities.CollectionData;

import java.util.List;
import java.util.stream.Collectors;

import esea.org.service.NeoOrgWsProxy;
import esea.org.vo.OrgUserVO;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.File;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Map;

@Service
public class DataCollectionService {
    private final Environment env;
    private final boolean AI_NEOSLO_ENABLED;
    
    public DataCollectionService(Environment env) {
        this.env = env;
        this.AI_NEOSLO_ENABLED = Boolean.parseBoolean(env.getProperty("neoslo.enabled", "true"));
    }

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionDataRepository collectionDataRepository;

    @Transactional
    public Boolean createCollection(CollectionManagementRequestDto body) {
        String status = "Y".equals(body.getStatus()) ? "COMPLETE" : "PENDING";
        String useYN = "Y".equals(body.getUseYn()) ? "Y" : "N";

        collectionRepository.createByQuery(body.getType().toString(), body.getSystem(), body.getName(), body.getUserId(), status, body.getS3Path(), body.getMethod(), useYN);
        
        return true;
    }

    @Transactional
    public Boolean updateCollection(CollectionManagementUpdateRequestDto body) {
        String status = "Y".equals(body.getStatus()) ? "COMPLETE" : "PENDING";
        String useYN = "Y".equals(body.getUseYn()) ? "Y" : "N";

        Integer collectionId = Integer.parseInt(body.getCollectionId());
        collectionRepository.updateByQuery(collectionId, body.getType().toString(), body.getSystem(), body.getName(), body.getUserId(), body.getS3Path(), status, body.getMethod(), useYN);
        
        return true;
    }

    // 목록 불러오기
    @Transactional
    public CollectionListResponseDto getCollectionList(CollectionListRequestDto body) {
        PageRequest pageRequest = PageRequest.of(body.getPage(), body.getSize());
        Page<Collection> pageResult = collectionRepository.findAllByOrderByCollectionIdAsc(pageRequest);
        
        List<CollectionResponseDto> content = pageResult.getContent().stream()
            .map(CollectionResponseDto::new)
            .collect(Collectors.toList());
        
        // 사용자 이름 조회
        final NeoOrgWsProxy proxy = AI_NEOSLO_ENABLED ? new NeoOrgWsProxy() : null;
        content.forEach(item -> {
            if(item.getUserId() == "SYSTEM") {
                item.setUserName("시스템");
            } else {
                if(proxy != null) {
                    try {
                        String userId = item.getUserId();
                        OrgUserVO[] userData = proxy.searchUserByUserId(userId);
                        item.setUserName(userData[0].getUserName());
                    } catch (RemoteException e) {
                        System.err.println("사용자 이름 조회 실패: " + item.getUserId());
                    } catch (Exception e) {
                        System.err.println("예기치 않은 오류 발생: " + e.getMessage());
                    }
                }
            }
        });

        CollectionListResponseDto response = new CollectionListResponseDto();
        response.setContent(content);
        response.setTotalCount((int) pageResult.getTotalElements());

        return response;
    }

    // 수집 Data 관리 상세
    @Transactional
    public CollectionDetailResponseDto getCollectionManagementDetail(CollectionManagementDetailRequestDto body) {
        Collection collection = collectionRepository.findByCollectionId(Integer.parseInt(body.getCollectionId()));

        long totalCount = collectionDataRepository.countByCollectionId(Integer.parseInt(body.getCollectionId()));

        CollectionDetailResponseDto response = new CollectionDetailResponseDto(collection);
        response.setTotalCount(totalCount);

        return response;
    }

    // 수집 Data 등록 로그
    @Transactional
    public CollectionDataLogResponseDto getCollectionLog(CollectionDataRequestDto requestDto) {
        PageRequest pageRequest = PageRequest.of(requestDto.getPage(), requestDto.getSize());
        
        // 검색 조건 설정
        OffsetDateTime startDate = null;
        OffsetDateTime endDate = null;

        // 날짜 유효성 검사
        if ((requestDto.getStartDate() != null && requestDto.getEndDate() == null) ||
            (requestDto.getStartDate() == null && requestDto.getEndDate() != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작일과 종료일 모 입력해야 합니다.");
        }

        // 날짜 조건이 있는 경우에만 변환
        if (requestDto.getStartDate() != null && requestDto.getEndDate() != null) {
            LocalDate start = LocalDate.parse(requestDto.getStartDate(),
                    DateTimeFormatter.ofPattern("yyyyMMdd"));
            LocalDate end = LocalDate.parse(requestDto.getEndDate(),
                    DateTimeFormatter.ofPattern("yyyyMMdd"));

            startDate = start.atStartOfDay()
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toOffsetDateTime();

            endDate = end.atTime(23, 59, 59)
                    .atZone(ZoneId.of("Asia/Seoul"))
                    .toOffsetDateTime();
        }

        // collection_id를 Integer로 변환
        Integer collectionId = Integer.parseInt(requestDto.getCollectionId());
        
        Page<CollectionData> pageDatas = collectionDataRepository.findByDateAndKeyword(
            collectionId,  // String에서 Integer로 변환된 값 사용
            startDate, 
            endDate, 
            requestDto.getKeyword(), 
            pageRequest
        );

        CollectionDataLogResponseDto response = new CollectionDataLogResponseDto();

        List<CollectionDataResponseDto> content = pageDatas.getContent().stream().map(CollectionDataResponseDto::new).collect(Collectors.toList());

        response.setContent(content);
        response.setTotalCount((int) pageDatas.getTotalElements());

        return response;
    }

    @Transactional
    public Boolean deleteCollection(CollectionDeleteRequestDto body) {
        collectionDataRepository.deleteAllById(body.getIds());
        return true;
    }

    @Transactional
    public Map<String, String> registerCollection(MultipartFile file, String name, String collectionId, String userId) {
        Collection collection = collectionRepository.findByCollectionId(Integer.parseInt(collectionId));
        if(collection == null) {
            return Map.of("error", "수집 데이터가 존재하지 않습니다.");
        }

        String s3UploadPath;

        try {
            // 시스템의 임시 디렉토리 경로 가져오기
            String tempDir = System.getProperty("java.io.tmpdir");
            String filePath = tempDir + File.separator + file.getOriginalFilename();

            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);

            String bucketName = "ddi-ai-datalake";
            s3UploadPath = collection.getFolderPath() + "/" + file.getOriginalFilename();
            // s3UploadPath 맨 앞의 "/" 제거
            if (s3UploadPath.startsWith("/")) {
                s3UploadPath = s3UploadPath.substring(1);
            }

            String localFilePath = destinationFile.getAbsolutePath();
            
            // S3 파일 업로드
            S3Client s3 = S3Util.getS3Client();
            Path targetFilePath = Paths.get(localFilePath);
            
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3UploadPath) // S3 내 파일 경로 및 이름 (예: "folder-name/filename.txt")
                    .build();
            
            // S3에 파일 업로드
            s3.putObject(putObjectRequest, targetFilePath);
        } catch (IOException e) {
            return Map.of("error", "업로드 파일 저장실패 하였습니다.");
        }

        // 데이터 저장
        CollectionData collectionData = new CollectionData();
        collectionData.setCollectionId(Integer.parseInt(collectionId));
        collectionData.setUserId(userId);
        collectionData.setFilePath(s3UploadPath);
        collectionData.setName(name);
        collectionData.setFileName(s3UploadPath.split("/")[s3UploadPath.split("/").length - 1]);

        collectionDataRepository.save(collectionData);

        System.out.println(s3UploadPath);

        return Map.of("filePath", s3UploadPath.startsWith("/") ? s3UploadPath.substring(1) : s3UploadPath);
    }
}
