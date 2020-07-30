package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.example.demo.entities.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CustomerService {
	
	public List<Customer> searchAllCustomers() throws JsonProcessingException;
	
	public List<Customer> findAllCustomers();

	public Optional<Customer> findCustomerById(Long id);

	public Customer saveCustomer(Customer customerNew);

	public String deleteCustomer(Long id);

	public String updateCustomer(Customer customerNew);

	public Map<String, Customer> findAllRedis();

	public Customer findByIdRedis(String id);

	public void saveRedis(Customer student);

	public void deleteRedis();
	
	public void refreshRedisData(List<Customer> customerList);
}
