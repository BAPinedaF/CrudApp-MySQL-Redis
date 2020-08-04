package com.example.demo.configuration;

import com.example.demo.entities.Customer;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {
	
	@Bean
    public JedisConnectionFactory redisConnectionFactory() {
        System.out.println("redisConnectionFactory");
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        // Git
        redisConnectionFactory.setHostName("redis");
        // Local
        //redisConnectionFactory.setHostName("localhost");
        redisConnectionFactory.setPort(6379);
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Customer> redisTemplate(RedisConnectionFactory cf) {
        System.out.println("redisTemplate");
        RedisTemplate<String, Customer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
   
}
