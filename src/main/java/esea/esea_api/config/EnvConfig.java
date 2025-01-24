package esea.esea_api.config;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EnvConfig {
    
    private final Environment env;

    public EnvConfig(Environment env) {
        this.env = env;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        log.info("현재 데이터베이스 URL: {}", env.getProperty("spring.datasource.url"));
        log.info("현재 DynamoDB Endpoint: {}", env.getProperty("aws.dynamodb.endpoint"));
    }
}
