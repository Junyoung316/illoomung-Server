package com.reserve.illoomung.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * Swagger UI 접속 방법
 * 	•	문서 페이지: `http://localhost:8080/swagger-ui.html`
 *      •	OpenAPI JSON: `http://localhost:8080/v3/api-docs`
 */

 @Configuration
//  @SecurityScheme(  // JWT 보안 스키마 추가
//         name = "BearerAuth",
//         type = SecuritySchemeType.HTTP,
//         scheme = "bearer",
//         bearerFormat = "JWT"
// )
public class SwaggerConfig { // Swagger 설정
 
    @Bean
    public OpenAPI openAPI() {
        // 로컬 서버 설정
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("로컬 개발 환경");

        // 운영 서버 설정 (실제 URL로 변경 필요)
        Server prodServer = new Server()
                .url("https://api.your-domain.com")
                .description("운영 서버");

        // API 정보 설정
        Info info = new Info()
                .title("API 문서 제목")
                .version("1.0.0")
                .description("API 상세 설명")
                .contact(new Contact()  // 담당자 정보
                        .name("개발팀")
                        .email("mungtime2025@gmail.com"))
                .license(new License()  // 라이선스 정보
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"));

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, prodServer));
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("*")
                        .allowedHeaders("*"); // 또는 "Authorization"
            }
        };
    }
 }
 
