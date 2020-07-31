package com.example.demo.controllersimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.controllers.CustomerController;
import com.example.demo.entities.Customer;
import com.example.demo.service.CustomerServiceMySQL;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootApplication
@RestController
@CrossOrigin
public class CustomerControllerImpl implements CustomerController {
	
	@Autowired
	CustomerServiceMySQL customerService;
	
	// http://localhost:8888/customers (GET)
	@Override
	@GetMapping("/customers")
	public List<Customer> getAllCustomers() throws JsonProcessingException {
		return customerService.searchAllCustomers();
	}

	// http://localhost:8888/customers/1 (GET)
	@GetMapping("/customers/{id}")
	//@RequestMapping(value = "/customers/{id}", method = RequestMethod.GET, produces = "application/json")
	public Optional<Customer> getCustomerById(@PathVariable Long id) {
		return customerService.findCustomerById(id);
	}

	// http://localhost:8888/customers/add (ADD)
	@Override
	@PostMapping("/customers/add")
	public Customer addCustomer(@RequestBody Customer customer) {
		return customerService.saveCustomer(customer);
	}

	// http://localhost:8888/customers/delete/1 (GET)
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable Long id) throws JsonProcessingException {
		String status = customerService.deleteCustomer(id);
		getAllCustomers();
		return status;
	}

	// http://localhost:8888/customers/update (PATCH)
	@Override
	@RequestMapping(value = "/customers/update", method = RequestMethod.PATCH, produces = "application/json")
	public String updateCustomer(Customer customerNew) {
		return customerService.updateCustomer(customerNew);
	}
}