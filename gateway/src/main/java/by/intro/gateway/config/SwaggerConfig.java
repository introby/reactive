package by.intro.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

//    to API page http://localhost:8081/swagger-ui.html

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Reactive Gateway API")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .email("siarhei.povad@innowise-group.com")
                                                .url("https://innowise-group.com")
                                                .name("Siarhei Povad")
                                )
                );
    }

}
