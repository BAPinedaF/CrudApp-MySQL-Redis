package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Customer;

@Service
public class CustomerServiceRedis {
	private long eventsLifeTime = 10;

	private static final String KEY = "Customer";

	@Autowired
	private RedisTemplate<String, Customer> redisTemplate;
	private HashOperations<String, String, Customer> hashOperations;
	
	public CustomerServiceRedis(RedisTemplate<String, Customer> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}
	
	public List<Customer> findAllRedis() {
		//hashOperations.entries(KEY)
		Set<String> keys = redisTemplate.keys("customer#*");
		List<Customer> customerList = new ArrayList<Customer>();
		for (String key : keys) {
			customerList.add(redisTemplate.opsForValue().get(key));
		}
		Collections.reverse(customerList);
		return customerList;
	}

	public Customer findByIdRedis(String id) {
		return (Customer) hashOperations.get(KEY, id);
	}

	public void saveRedis(Customer customerNew) {
		redisTemplate.opsForValue().set("customer#" + customerNew.getId(),customerNew);
		redisTemplate.expire("customer#" + customerNew.getId(), eventsLifeTime, TimeUnit.MINUTES );
	}

	public void deleteRedis() {
		redisTemplate.delete(KEY);
	}
	
	public void refreshRedisData(List<Customer> customerList) {
		for (Customer customer : customerList) {
			saveRedis(customer);
		}
	}
}
