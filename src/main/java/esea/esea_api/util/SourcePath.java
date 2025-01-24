package esea.esea_api.util;

import org.springframework.stereotype.Component;

import esea.esea_api.translation.util.S3Util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;

import java.util.Map;

@Component
public class SourcePath {
    private String amazonAWSAccessKey;
    private String amazonAWSSecretKey;
    private String lakeBucket;

    private final Environment env;
    private final String LAKE_BUCKET_URL;
    private final S3Client s3;

    public SourcePath(Environment env) {
        this.env = env;
        this.LAKE_BUCKET_URL = env.getProperty("LAKE_BUCKET_URL");
        this.amazonAWSAccessKey = env.getProperty("AI_AWS_ACCESS_KEY");
        this.amazonAWSSecretKey = env.getProperty("AI_AWS_SECRET_KEY");
        this.lakeBucket = env.getProperty("LAKE_BUCKET");
        
        // 리전 설정 (필요한 리전으로 변경)
        Region region = Region.AP_NORTHEAST_2; // 서울 리전

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                amazonAWSAccessKey,  // @Value로 주입된 AWS Access Key ID
                amazonAWSSecretKey   // @Value로 주입된 AWS Secret Access Key
        );

        this.s3 = S3Client.builder()
                .region(region)  // 사용하는 리전으로 설정
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))  // 자격 증명 설정
                .build();
    }

    // S3 파일 경로 빌드
    public String buildS3Url(String path) {
        if(!path.startsWith("/")) {
            path = "/" + path;
        }

        // PDF 체크
        if(path.endsWith(".pdf")) {
            updatePdfMetadata(path.substring(1));
        }

        return (LAKE_BUCKET_URL + path);
    }

    private void updatePdfMetadata(String key) {
        try {
            CopyObjectRequest request = CopyObjectRequest.builder()
                .sourceBucket(lakeBucket)
                .sourceKey(key)
                .destinationBucket(lakeBucket)
                .destinationKey(key)
                .contentType("application/pdf")
                .metadata(Map.of(
                    "Content-Type", "application/pdf"
                ))
                .metadataDirective("REPLACE")
                .build();

            s3.copyObject(request);
        } catch (Exception e) {
            // 에러 로깅
            e.printStackTrace();
        }
    }
}
