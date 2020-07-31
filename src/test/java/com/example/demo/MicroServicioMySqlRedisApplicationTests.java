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
    	customerNew.setId((long) 4);
    	customerNew.setName("Jason");
    	customerNew.setLastname("Todd");
    	customerNew.setPhone("3333333");
    	Customer customerSaved = new Customer();
    	customerSaved = customerService.saveCustomer(customerNew);
    	assertEquals(4, customerSaved.getId());
    }
    
    

}
