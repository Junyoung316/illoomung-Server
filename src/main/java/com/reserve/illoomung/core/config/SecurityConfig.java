package com.reserve.illoomung.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableAspectJAutoProxy
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() { // 권한 없는 사용자 제한 
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401: 미인증 사용자 요청, 프론트엔드에서 로그인 페이지로 이동 처리
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"status\": 401, \"error\": \"인증이 필요합니다.\"}");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout(logout -> logout.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/public", 
                    "/login", 
                    "/register/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers("/owner/**").hasAnyRole("OWNER", "ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/logout").authenticated()
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authenticationEntryPoint())
            );

        return http.build();
    }
}
