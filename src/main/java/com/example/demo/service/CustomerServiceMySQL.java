package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Customer;
import com.example.demo.repository.CustomerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;


@Service
@EnableScheduling
public class CustomerServiceMySQL {

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	CustomerServiceRedis customerServiceRedis;

	public List<Customer> findAllCustomers() {
		return customerRepository.findAll();
	}

	public Optional<Customer> findCustomerById(Long id) {
		return customerRepository.findById(id);
	}

	public Customer saveCustomer(Customer customerNew) {
		if (customerNew != null) {
			return customerRepository.save(customerNew);
		}
		return new Customer();
	}

	public String deleteCustomer(Long id) {
		if (customerRepository.findById(id).isPresent()) {
			customerRepository.deleteById(id);
			return "{ \"status\": \"Eliminado con exito\" }";
		}
		return "{ \"status\": \"El usuario con el id " + id  + " no existe y no se puede eliminar\" }";
	}

	public String updateCustomer(Customer customerUpdated) {
		Long id = customerUpdated.getId();
		
		if (customerRepository.findById(id).isPresent()) {
			Customer customerToUpdate = new Customer();
			customerToUpdate.setId(customerUpdated.getId());
			customerToUpdate.setName(customerUpdated.getName());
			customerToUpdate.setLastname(customerUpdated.getLastname());
			customerToUpdate.setPhone(customerUpdated.getPhone());
			customerRepository.save(customerToUpdate);
			return "{ \"status\": \"Customer actualizado\" }";
		}
		return "{ \"status\": \"El customer con el id " + id  + " no existe y no se puede actualizar\" }";
	}

	public List<Customer> searchAllCustomers() {
		List<Customer> redisData = customerServiceRedis.findAllRedis();
		if (redisData.toString().equals("[]")) {
			System.out.println("-----------------------------------SQL-----------------------------------");
			customerServiceRedis.refreshRedisData(findAllCustomers());
			return findAllCustomers();
		} else {
			System.out.println("----------------------------------REDIS----------------------------------");
			return redisData;
		}
	}


}
