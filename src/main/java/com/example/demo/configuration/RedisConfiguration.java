package com.example.demo.configuration;

import com.example.demo.entities.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
	@SuppressWarnings("deprecation")
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
	        JedisConnectionFactory factory = new JedisConnectionFactory();
	        factory.setHostName("localhost");
	        factory.setPort(6379);
	        return factory;
	}

    @Bean
    @Autowired
    RedisTemplate<String, Customer> redisTemplate() {
        final RedisTemplate<String, Customer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
   
}
