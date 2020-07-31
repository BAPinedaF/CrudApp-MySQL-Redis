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
		//customerServiceRedis.deleteRedis();
		if (customerRepository.findById(id).isPresent()) {
			customerRepository.deleteById(id);
			return "Eliminado con exito";
		}
		return "El usuario con el id " + id  + " no existe y no se puede eliminar";
	}

	public String updateCustomer(Customer customerUpdated) {
		Long num = customerUpdated.getId();
		
		if (customerRepository.findById(num).isPresent()) {
			Customer customerToUpdate = new Customer();
			customerToUpdate.setId(customerUpdated.getId());
			customerToUpdate.setName(customerUpdated.getName());
			customerToUpdate.setPhone(customerUpdated.getPhone());
			customerRepository.save(customerToUpdate);
			return "Customer modificado";
		}
		return "Error al modificar el Customer";
	}

	public List<Customer> searchAllCustomers() throws JsonProcessingException {
		String redisData = customerServiceRedis.findAllRedis().toString();
		if (redisData.equals("[]")) {
			System.out.println("-----------------------------------SQL-----------------------------------");
			customerServiceRedis.refreshRedisData(findAllCustomers());
			return findAllCustomers();
		} else {
			System.out.println("----------------------------------REDIS----------------------------------");
			return customerServiceRedis.findAllRedis();
		}
	}


}
