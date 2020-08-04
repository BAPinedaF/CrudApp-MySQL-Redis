package com.example.demo.configuration;

import com.example.demo.entities.Customer;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
	static JedisPool pool;
	static JedisPoolConfig config;
	
	@SuppressWarnings("deprecation")
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
	        JedisConnectionFactory factory = new JedisConnectionFactory();
	        factory.setHostName("localhost");
	        factory.setPort(6379);
	        factory.setUsePool(true);
	        config = new JedisPoolConfig();
			config.setMaxTotal(1000000);
			config.setMaxIdle(1000000);
			pool = new JedisPool(config, "localhost", 6379, 0);


	        return factory;
	}

    @Bean
    @Autowired
    RedisTemplate<String, Customer> redisTemplate() {
        final RedisTemplate<String, Customer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
   
}
