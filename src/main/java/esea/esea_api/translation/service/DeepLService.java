package esea.esea_api.translation.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

import esea.esea_api.translation.dto.DeepLResponse;
import esea.esea_api.translation.dto.DeepLUploadResponse;
import esea.esea_api.translation.util.S3Util;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Log4j2
@Service
public class DeepLService {
    
	// private final String API_KEY = "407cf278-2277-4dce-95e9-2524ae384c36:fx";  // DeepL API 키를 입력하세요
	// private final String API_URL = "https://api.deepl.com/v2/translate";
	// private final String API_URL_DOC = "https://api.deepl.com/v2/document";
	// private static final String USAGE_API_URL = "https://api.deepl.com/v2/usage";

    // private final String API_KEY = env.getProperty("DEEL_API_KEY");
    // private final String API_URL = env.getProperty("DEEL_API_URL");
    // private final String API_URL_DOC = env.getProperty("DEEL_API_URL_DOC");
    // private final String USAGE_API_URL = env.getProperty("DEEL_USAGE_API_URL");
    // OkHttpClient client = new OkHttpClient();

    private final Environment env;
    private final String API_KEY;
    private final String API_URL;
    private final String API_URL_DOC;
    private final String USAGE_API_URL;
    private final String DEEL_AWS_BUCKET;
    private final String DEEL_AWS_PATH;
    private final OkHttpClient client = new OkHttpClient();

    @Autowired
    public DeepLService(Environment env) {
        this.env = env;
        this.API_KEY = env.getProperty("DEEL_API_KEY");
        this.API_URL = env.getProperty("DEEL_API_URL");
        this.API_URL_DOC = env.getProperty("DEEL_API_URL_DOC");
        this.USAGE_API_URL = env.getProperty("DEEL_USAGE_API_URL");
        this.DEEL_AWS_BUCKET = env.getProperty("DEEL_AWS_BUCKET");
        this.DEEL_AWS_PATH = env.getProperty("DEEL_AWS_PATH");
    }

	/*
	 * public String translateText(String text, String targetLang) throws Exception
	 * {
	 * 
	 * Request request = new Request.Builder() .url(API_URL + "?auth_key=" + API_KEY
	 * + "&text=" + text + "&target_lang=" + targetLang) .build();
	 * 
	 * try (Response response = client.newCall(request).execute()) { if
	 * (!response.isSuccessful()) throw new Exception("Unexpected code " +
	 * response); JSONObject jsonResponse = new
	 * JSONObject(response.body().string()); return
	 * jsonResponse.getJSONArray("translations").getJSONObject(0).getString("text");
	 * } }
	 */
    
    public Map<String, Object> getUsageInfo(String apiKey) throws Exception {
        Request request = new Request.Builder()
            .url(USAGE_API_URL)
            .header("Authorization", "DeepL-Auth-Key " + apiKey)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Failed to check usage: " + response);
            }
            
            JSONObject jsonResponse = new JSONObject(response.body().string());
            Map<String, Object> usageInfo = new HashMap<>();
            
            int charactersCount = jsonResponse.getInt("character_count");
            int characterLimit = jsonResponse.getInt("character_limit");
            double usagePercentage = (charactersCount / (double)characterLimit) * 100;
            
            usageInfo.put("charactersUsed", charactersCount);
            usageInfo.put("characterLimit", characterLimit);
            usageInfo.put("remainingCharacters", characterLimit - charactersCount);
            usageInfo.put("usagePercentage", String.format("%.2f%%", usagePercentage));
            
            return usageInfo;
        }
    }
    
    // 파일 업로드 후 S3 URL 반환
    public String uploadFile(String bucketName, String s3Path, String localFilePath) {
        S3Client s3 = S3Util.getS3Client();
        Path filePath = Paths.get(localFilePath);
        
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path) // S3 내 파일 경로 및 이름 (예: "folder-name/filename.txt")
                .build();

        
        // S3에 파일 업로드
        s3.putObject(putObjectRequest, filePath);

        // S3 파일 URL 생성
        String fileUrl = String.format(DEEL_AWS_PATH, bucketName, s3Path);

        return fileUrl;
    }
    
    // 파일 다운로드 후 S3 URL 반환
    public String downloadFile(String bucketName, String s3Path, String localDownloadPath) {
        S3Client s3 = S3Util.getS3Client();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Path) // 다운로드할 S3 경로 (예: "folder-name/filename.txt")
                .build();

        // S3에서 파일 다운로드
        s3.getObject(getObjectRequest, Paths.get(localDownloadPath));

        // S3 파일 URL 생성
        String fileUrl = String.format(DEEL_AWS_PATH, bucketName, s3Path);

        // JSON 객체 생성
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("fileName", s3Path);
        jsonResponse.addProperty("fileUrl", fileUrl);

        // JSON 응답 문자열로 반환
        return jsonResponse.toString();
    }
    
    // DeepL API 무료 버전 기준으로 500,000자 제한
    private static final int MAX_CHARACTER_LIMIT = 500000;

    /**
     * 파일의 글자 수를 계산합니다.
     */
    public int countCharacters(MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        return content.length();
    }
    
    /**
     * MultipartFile을 File로 변환합니다.
     */
    private File convertToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
    
    /**
     * 타임아웃 설정
     */
    public RestTemplate createRestTemplateWithTimeout() {
        // 타임아웃을 1분 (60000ms)로 설정
        int timeout = 60000;  // 1분

        // SimpleClientHttpRequestFactory에 연결 및 읽기 타임아웃 설정
        ClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        ((SimpleClientHttpRequestFactory) factory).setConnectTimeout(timeout);  // 연결 타임아웃
        ((SimpleClientHttpRequestFactory) factory).setReadTimeout(timeout);     // 읽기 타임아웃

        // RestTemplate에 요청 factory 적용
        return new RestTemplate(factory);
    }
    
    /**
     * DeepL API에 문서를 업로드하고 응답을 반환합니다.
     */
    public DeepLResponse uploadDocumentBefore (MultipartFile file, String sourceLang, String targetLang, String apiKey) throws IOException {
        File tempFile = convertToFile(file);
        ResponseEntity<DeepLUploadResponse> response = null;

        try {
        	RestTemplate restTemplate = createRestTemplateWithTimeout();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "DeepL-Auth-Key " + apiKey);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(tempFile)); // 올바른 파일 첨부
            body.add("source_lang", sourceLang);
            body.add("target_lang", targetLang);
            
            // 번역 요청 보내기
            try {
            	HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                response = restTemplate.postForEntity(API_URL_DOC, requestEntity, DeepLUploadResponse.class);
                log.info("response: " + response.getStatusCode() + " / " + response.getBody());
                System.out.println("response: " + response.getStatusCode() + " / " + response.getBody());
                if (response.getStatusCode() != HttpStatus.OK) {
                	log.info("Error: " + response.getStatusCode() + " / " + response.getBody());
                    System.out.println("Error: " + response.getStatusCode());
                }
            } catch (Exception e) {
                // 요청 전송 중 오류 발생
            	log.info("Error occurred during request: " + e.getMessage());
                System.out.println("Error occurred during request: " + e.getMessage());
                e.printStackTrace();
                throw new IOException("Error occurred during request to DeepL API", e);
            }
            
            // API 응답에서 documentId와 documentKey 추출
            DeepLUploadResponse uploadResponse = response.getBody();
            if (uploadResponse == null || uploadResponse.getDocumentId() == null || uploadResponse.getDocumentKey() == null) {
                throw new IOException("Invalid response from DeepL API");
            }

            // 시스템의 임시 디렉토리 경로 가져오기
            String tempDir = System.getProperty("java.io.tmpdir");
            String filePath = tempDir + File.separator + file.getOriginalFilename();
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);
            
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String formattedDateTime = now.format(formatter);
            
            // S3 업로드
            String bucketName = DEEL_AWS_BUCKET;
            String s3UploadPath = "up/"+formattedDateTime+"_"+file.getOriginalFilename();
            String localFilePath = destinationFile.getAbsolutePath();
            
            // 파일 업로드
            String uploadFile = this.uploadFile(bucketName, s3UploadPath, localFilePath);
            
            // 파일 번역 업로드
            // 다운로드 URL 생성
            String downloadUrl = API_URL_DOC + "/" + uploadResponse.getDocumentId() + "/result";
            RestTemplate downRestTemplate = new RestTemplate();
            byte[] fileBytes = downRestTemplate.getForObject(
                    downloadUrl + "?auth_key=" + apiKey + "&document_key=" + uploadResponse.getDocumentKey(),
                    byte[].class
            );
            int pos = 0;
            String ext = "";
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                pos = originalFilename.lastIndexOf(".");
                ext = "." + originalFilename.substring(pos + 1);
            } else {
                throw new IllegalArgumentException("파일 이름이 없습니다.");
            }
            
            // 로컬에 임시 파일로 저장
            File downTempFile = File.createTempFile("translated_", ext);
            try (FileOutputStream fos = new FileOutputStream(downTempFile)) {
                fos.write(fileBytes);
            }
            
            // 로컬 경로를 사용하여 S3에 업로드
            String localDownloadPath = downTempFile.getAbsolutePath();
            String s3DownloadPath = "down/"+downTempFile.getName();
            String downloadFile = this.uploadFile(bucketName, s3DownloadPath, localDownloadPath);
            
            // 임시 파일 삭제
            downTempFile.delete();
            return new DeepLResponse(file.getOriginalFilename(), downTempFile.getName(),uploadFile , downloadFile, s3UploadPath, s3DownloadPath);
        } finally {
            // Temp 파일 삭제
            tempFile.delete();
        }
    }

    /**
     * DeepL API에 문서를 업로드하고 응답을 반환합니다.
     */
    public DeepLResponse uploadDocument(MultipartFile file, String sourceLang, String targetLang, String apiKey) throws IOException {
        // 업로드를 위한 임시파일 변환
        File tempFile = convertToFile(file);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "DeepL-Auth-Key " + apiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 바디 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(tempFile)); // 올바른 파일 첨부
        body.add("source_lang", sourceLang);
        body.add("target_lang", targetLang);
        
        // 업로드
        try {
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 요청 보내기
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<DeepLUploadResponse> response = restTemplate.exchange(
                API_URL_DOC,
                HttpMethod.POST,
                requestEntity,
                DeepLUploadResponse.class
            );
            
            // 응답 로깅 추가
            log.info("Upload response: " + response.getBody());
            
            // deelpPro 응답 확인
            DeepLUploadResponse uploadResponse = response.getBody();
            if (uploadResponse == null) {
                throw new IOException("Response body is null");
            }
            
            log.info("Document ID: " + uploadResponse.getDocumentId());
            log.info("Document Key: " + uploadResponse.getDocumentKey());
            
            // 번역 상태 확인을 위한 폴링 로직 추가
            String statusUrl = API_URL_DOC + "/" + uploadResponse.getDocumentId();
            boolean isTranslationComplete = false;
            int maxAttempts = 30; // 최대 30번 시도 (5분)
            int attempt = 0;

            // 번역 상태 확인
            while (!isTranslationComplete && attempt < maxAttempts) {
                HttpHeaders statusHeaders = new HttpHeaders();
                statusHeaders.set("Authorization", "DeepL-Auth-Key " + apiKey);
                
                // document_key를 쿼리 파라미터로 추가
                String statusUrlWithKey = statusUrl + "?document_key=" + uploadResponse.getDocumentKey();
                
                // 상태 확인 요청
                HttpEntity<String> statusRequest = new HttpEntity<>(statusHeaders);
                ResponseEntity<String> statusResponse = restTemplate.exchange(
                    statusUrlWithKey,  // 수정된 URL 사용
                    HttpMethod.GET,
                    statusRequest,
                    String.class
                );

                // 상태 확인 응답 파싱
                JSONObject statusJson = new JSONObject(statusResponse.getBody());
                String status = statusJson.getString("status");
                
                // 번역 완료 여부 확인
                if ("done".equals(status)) {
                    isTranslationComplete = true;
                } else if ("error".equals(status)) {
                    throw new IOException("Translation failed: " + statusJson.optString("message"));
                } else {
                    // 10초 대기 후 다시 시도
                    Thread.sleep(10000);
                    attempt++;
                }
            }

            // 번역 상태 확인 실패 시 예외 발생
            if (!isTranslationComplete) {
                throw new IOException("Translation timed out after " + maxAttempts + " attempts");
            }

            // 시스템의 임시 디렉토리 경로 가져오기
            String tempDir = System.getProperty("java.io.tmpdir");
            String filePath = tempDir + File.separator + file.getOriginalFilename();
            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);
            
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String formattedDateTime = now.format(formatter);
            
            // String bucketName = "htc-ai-web";
            String bucketName = DEEL_AWS_BUCKET;
            String s3UploadPath = "up/"+formattedDateTime+"_"+file.getOriginalFilename();
            String localFilePath = destinationFile.getAbsolutePath();
            
            // 파일 업로드
            String uploadFile = this.uploadFile(bucketName, s3UploadPath, localFilePath);
            
            // 다운로드 URL 생성
            String downloadUrl = API_URL_DOC + "/" + uploadResponse.getDocumentId() + "/result";
            RestTemplate downRestTemplate = new RestTemplate();
            byte[] fileBytes = downRestTemplate.getForObject(
                    downloadUrl + "?auth_key=" + apiKey + "&document_key=" + uploadResponse.getDocumentKey(),
                    byte[].class
            );

            // 파일 확장자 추출
            int pos = 0;
            String ext = "";
            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null) {
                pos = originalFilename.lastIndexOf(".");
                ext = "." + originalFilename.substring(pos + 1);
            } else {
                throw new IllegalArgumentException("파일 이름이 없습니다.");
            }
            
            // 로컬에 임시 파일로 저장
            File downTempFile = File.createTempFile("translated_", ext);
            try (FileOutputStream fos = new FileOutputStream(downTempFile)) {
                fos.write(fileBytes);
            }
            
            // 로컬 경로를 사용하여 S3에 업로드
            String localDownloadPath = downTempFile.getAbsolutePath();
            String s3DownloadPath = "down/"+downTempFile.getName();
            String downloadFile = this.uploadFile(bucketName, s3DownloadPath, localDownloadPath);
            
            // 임시 파일 삭제
            downTempFile.delete();
            return new DeepLResponse(file.getOriginalFilename(), downTempFile.getName(),uploadFile , downloadFile, s3UploadPath, s3DownloadPath);
        } catch (Exception e) {
            log.error("Translation error: ", e);
            throw new IOException("Error during translation process", e);
        } finally {
            // Temp 파일 삭제
            tempFile.delete();
        }
    }
    
    /**
     * 번역된 문서를 다운로드하여 저장합니다.
     */
    private void saveTranslatedDocument(String documentId, String documentKey, String savePath) throws IOException {
        String downloadUrl = API_URL_DOC + "/" + documentId + "/result";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "DeepL-Auth-Key " + API_KEY);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("document_key", documentKey);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(downloadUrl, HttpMethod.POST, requestEntity, byte[].class);

        // 번역된 파일 저장
        File outputFile = new File(savePath);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(response.getBody());
        }
    }

}