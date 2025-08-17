package com.reserve.illoomung.core.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfig implements ApplicationContextAware { // Thymeleaf 설정
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();

        SpringResourceTemplateResolver defaultResolver = new SpringResourceTemplateResolver();
        defaultResolver.setApplicationContext(applicationContext);  // 주입 추가
        defaultResolver.setPrefix("classpath:/templates/mail/");
        defaultResolver.setSuffix(".html");
        defaultResolver.setTemplateMode(TemplateMode.HTML);
        defaultResolver.setOrder(1);

        engine.addTemplateResolver(defaultResolver);
        return engine;
    }
}
