package com.safitech.homeworx.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.safitech.homeworx.config.redis.deserializer.RespTeacherDeserializer;
import com.safitech.homeworx.config.redis.serializer.RespTeacherSerializer;
import com.safitech.homeworx.model.RespTeacher;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisConfig {

    @Value("${redis.host}")
    String host;

    @Value("${redis.port}")
    int port;

    @Value("${redis.password}")
    String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lcf = new LettuceConnectionFactory(host, port);
        lcf.setPassword(password);
        return lcf;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        SimpleModule module = new SimpleModule();
        module.addSerializer(RespTeacher.class, new RespTeacherSerializer());
        module.addDeserializer(RespTeacher.class, new RespTeacherDeserializer());
        objectMapper.registerModule(module);

        Jackson2JsonRedisSerializer<RespTeacher> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, RespTeacher.class);

        template.setDefaultSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }

}