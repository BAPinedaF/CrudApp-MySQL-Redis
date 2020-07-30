package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.service.CustomerService;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-mysql.sql")
class MicroServicioMySqlRedisApplicationTests {

	@Autowired
    CustomerService customerService;
 
    @Test
    public void test() {
        assertEquals(2, customerService.findAllCustomers().size());
    }
    
    

}
