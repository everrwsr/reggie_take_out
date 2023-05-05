package com.everr.reggie_take_out.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {
/*
        //定义一个返回类型为RedisTemplate<Object,Object>，参数为RedisConnectionFactory类型的方法
 */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        //创建一个RedisTemplate<Object, Object>类型的变量redisTemplate
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        //设置key序列化方式为StringRedisSerializer，用于将key转为字符串格式存储在redis中
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置redis连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);
        //返回redisTemplate
        return redisTemplate;
    }
}

