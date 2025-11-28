package com.reserve.illoomung.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // application.yml/properties에 설정된 파일 저장 디렉토리 (예: ./images/stores/)
    @Value("${file.img.dir}")
    private String fileDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 1. 상대 경로를 절대 경로로 변환
        String absolutePath = new File(fileDir).getAbsolutePath() + File.separator;

        // 2. Resource Handler 등록
        // 클라이언트 요청: /images/**
        // 실제 경로: file:./uploads/
        // /images/stores/파일명.jpg -> [uploads]/stores/파일명.jpg 에서 찾게 됩니다.
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + absolutePath);
    }
}