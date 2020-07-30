package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;
import com.example.demo.entities.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface CustomerController {

	public Optional<Customer> getCustomerById(Long id);
	
	public List<Customer> getAllCustomers() throws JsonMappingException, JsonProcessingException;

	public Customer addCustomer(Customer customer);

	public String deleteCustomer(Long id) throws JsonMappingException, JsonProcessingException;

	public String updateCustomer(Customer customerNew);
}