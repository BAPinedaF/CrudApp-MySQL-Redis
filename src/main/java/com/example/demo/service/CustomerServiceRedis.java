package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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

	@Autowired
	private RedisTemplate<String, Customer> redisTemplate;
	
	public CustomerServiceRedis(RedisTemplate<String, Customer> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	public List<Customer> findAllRedis() {
		Set<String> keys = redisTemplate.keys("customer#*");
		List<Customer> customerList = new ArrayList<Customer>();
		for (String key : keys) {
			customerList.add(redisTemplate.opsForValue().get(key));
		}
		if (!customerList.isEmpty()) {
			Collections.sort(customerList, new Comparator<Customer>() {
				@Override
				public int compare(Customer customer1, Customer customer2) {
					return customer1.getId().compareTo(customer2.getId());
				}
			});
			return customerList;
		} else {
			return customerList;
		}

	}

	public Customer findByIdRedis(String id) {
		return redisTemplate.opsForValue().get("customer#" + id);
	}

	public void updateCustomerRedis(Customer customerNew) {
		long timeToExpire = redisTemplate.getExpire("customer#" + customerNew.getId());
		redisTemplate.opsForValue().set("customer#" + customerNew.getId(),customerNew);
		redisTemplate.expire("customer#" + customerNew.getId(), timeToExpire, TimeUnit.SECONDS );
	}
	
	public void newCustomerRedis(Customer customerNew) {
		redisTemplate.getExpire("customer#" + customerNew.getId());
		redisTemplate.opsForValue().getAndSet("customer#" + customerNew.getId(),customerNew);
		redisTemplate.expire("customer#" + customerNew.getId(), eventsLifeTime, TimeUnit.MINUTES );
	}

	public void deleteRedis(long id) {
		redisTemplate.delete("customer#" + id);
	}
	
	public void refreshRedisData(List<Customer> customerList) {
		for (Customer customer : customerList) {
			newCustomerRedis(customer);
		}
	}
}
