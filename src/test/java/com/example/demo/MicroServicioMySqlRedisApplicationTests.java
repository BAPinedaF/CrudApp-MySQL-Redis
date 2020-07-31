package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.demo.entities.Customer;
import com.example.demo.service.CustomerService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-mysql.sql")
@CrossOrigin
class MicroServicioMySqlRedisApplicationTests {
	
	
	@Autowired
    CustomerService customerService;
 
    @Test
    public void validateAllCustumersSQL() {
        assertEquals(3, customerService.findAllCustomers().size());
    }
    
    /*@Test
    public void validateAllCustumersRedis() {
    	assertEquals(0, customerService.findAllRedis().size());
    }*/
    
    @Test
    public void validateNewCustomer() {
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
    public void validateDeleteOfCustomer() {
    	assertEquals("Eliminado con exito", customerService.deleteCustomer((long) 1));
    }
    
    @Test
    public void validateEditCustomer() {
    	Customer editCustomer = new Customer();
    	editCustomer.setId((long) 1);
    	editCustomer.setName("Jason");
    	editCustomer.setLastname("Todd");
    	editCustomer.setPhone("3333333");
    	Customer customerEdited = new Customer();
    	assertEquals("Customer modificado", customerService.updateCustomer(customerEdited));
    	//customerEdited = customerService.updateCustomer(customerNew);
    }
    
    

}
