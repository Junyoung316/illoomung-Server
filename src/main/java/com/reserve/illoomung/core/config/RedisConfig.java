package com.reserve.illoomung.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig { // Redis 설정
    @Bean
    // Bean 타입을 StringRedisTemplate으로 변경합니다.
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        // StringRedisTemplate은 이미 String 직렬화를 기본으로 합니다.
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
