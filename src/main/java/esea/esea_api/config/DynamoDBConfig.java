package esea.esea_api.config;

import org.springframework.context.annotation.Configuration;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;

@Configuration
@EnableDynamoDBRepositories(basePackages = "esea.esea_api.dynamodb_repository")
public class DynamoDBConfig {

    @Value("${aws.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.accessKey}")
    private String amazonAWSAccessKey;

    @Value("${aws.secretKey}")
    private String amazonAWSSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard();

        builder.withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, awsRegion))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey)));

        return builder.build();
    }
}