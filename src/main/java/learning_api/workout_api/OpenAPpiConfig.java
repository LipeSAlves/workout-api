package learning_api.workout_api;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPpiConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Workout API")
                        .version("1.0")
                        .description("""
                                REST API for gym workout management: users, exercises, workout plans and personalized workout sheets.
                                
                                **Authentication:** use `POST /auth/login` to obtain a JWT, then click **Authorize** and paste the token.
                                Public endpoints: user registration, login, forgot-password and reset-password.
                                """))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT obtained from POST /auth/login")));
    }
}
