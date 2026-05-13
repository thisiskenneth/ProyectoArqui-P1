package ec.edu.espe.mstallersoap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("ms-taller-rest")
                .description("API REST del microservicio Taller. Expone consulta de vehiculos y registro de ordenes de mantenimiento.")
                .version("1.0.0"));
    }
}
