package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.demo.entities.Customer;
import com.example.demo.service.CustomerServiceMySQL;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-mysql.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@CrossOrigin
class CustomerServiceMySQLTests {
	
	
	@Autowired
    CustomerServiceMySQL customerService;
 
    @Test
    @Order(1)
    void validateGetAllCustomersSQL() {
    	List<Customer> customerList = new ArrayList<Customer>();
    	customerList = customerService.findAllCustomers();
        assertEquals(3, customerList.size());
    }
   
   @Test
   void validateSearchCustomer() {
	   Customer customer = new Customer();
	   Optional<Customer> customerFound = Optional.of(customer);
	   customerFound = customerService.findCustomerById(2L);
	   customer = customerFound.get();
	   assertEquals("Diana", customer.getName());
	   assertEquals("Prince", customer.getLastname());
	   assertEquals("1111111", customer.getPhone());
   }
    
    @Test
    void validateNewCustomer() {
    	Customer customerNew = new Customer();
    	customerNew.setName("Jason");
    	customerNew.setLastname("Todd");
    	customerNew.setPhone("3333333");
    	Customer customerSaved = new Customer();
    	customerSaved = customerService.saveCustomer(customerNew);
    	assertEquals("Jason", customerSaved.getName());
    	assertEquals("Todd", customerSaved.getLastname());
    	assertEquals("3333333", customerSaved.getPhone());
    }
    
    @Test
    void validateEditCustomer() {
    	Customer editCustomer = new Customer();
    	editCustomer.setId(1L);
    	editCustomer.setName("Jason");
    	editCustomer.setLastname("Todd");
    	editCustomer.setPhone("3333333");
    	assertEquals("{ \"status\": \"Customer actualizado\" }", customerService.updateCustomer(editCustomer));
    }
    
    @Test
    void validateDeleteCustomer() {
    	assertEquals("{ \"status\": \"Eliminado con exito\" }", customerService.deleteCustomer(3L));
    }
    
    @Test
    void validateErrorSearchCustomer() {
 	   Customer customer = new Customer();
 	   Optional<Customer> customerFound = Optional.of(customer);
 	   customerFound = customerService.findCustomerById(4L);
 	   assertEquals(Optional.empty(), customerFound);
    }
    
    @Test
    void validateErrorDeleteCustomer() {
    	long id = 4L;
    	assertEquals("{ \"status\": \"El usuario con el id 4 no existe y no se puede eliminar\" }", customerService.deleteCustomer(id));
    }
    
    @Test
    void validateErrorEditCustomer() {
    	Customer editCustomer = new Customer();
    	long id = 4L;
    	editCustomer.setId(id);
    	editCustomer.setName("Jason");
    	editCustomer.setLastname("Todd");
    	editCustomer.setPhone("3333333");
    	assertEquals("{ \"status\": \"El customer con el id "+id+" no existe y no se puede actualizar\" }", customerService.updateCustomer(editCustomer));
    }

}
