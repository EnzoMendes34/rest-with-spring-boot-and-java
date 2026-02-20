package EnzoMendes.com.github.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi() {
        //link to access docs in JSON format: http://localhost:8080/v3/api-docs
        //Link to access docs: http://localhost:8080/swagger-ui/index.html
        return new OpenAPI()
                .info(new Info()
                        .title("Rest API's RESTful from 0 with Java, SpringBoot, Kubernetes and docker")
                        .version("v1")
                        .description("Developed during a SpringBoot course")
                        .termsOfService("https://github.com/EnzoMendes34")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://github.com/EnzoMendes34")));
    }
}
