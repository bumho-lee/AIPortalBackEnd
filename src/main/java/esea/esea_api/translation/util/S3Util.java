package esea.esea_api.translation.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Util {
    @Value("${aws.accessKey}")
    private String amazonAWSAccessKey;

    @Value("${aws.secretKey}")
    private String amazonAWSSecretKey;

    // 리전 설정 (필요한 리전으로 변경)
    private static final Region region = Region.AP_NORTHEAST_2; // 서울 리전

    private static S3Client s3;

    // 인스턴스 초기화 블록을 사용하여 S3Client 초기화
    @PostConstruct
    public void init() {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                amazonAWSAccessKey,  // @Value로 주입된 AWS Access Key ID
                amazonAWSSecretKey   // @Value로 주입된 AWS Secret Access Key
        );

        s3 = S3Client.builder()
                .region(region)  // 사용하는 리전으로 설정
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))  // 자격 증명 설정
                .build();
    }

    // S3 클라이언트 반환
    public static S3Client getS3Client() {
        return s3;
    }

    public static void main(String[] args) {
        // S3 클라이언트 사용 예시
        S3Client s3Client = getS3Client();
        
        // S3 클라이언트를 이용하여 S3 작업 수행
        // 예: 버킷 목록 가져오기
        System.out.println("S3 Buckets: " + s3Client.listBuckets());
    }
}
