package esea.esea_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

// Swagger 설정
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "AI 토탈 API 문서", version = "1.0.0"))
@EnableScheduling
@ComponentScan(basePackages = {"esea.esea_api", "esea.backend.neoslo", "esea.prodSlo.service"})
public class EseaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EseaApiApplication.class, args);
	}

}
