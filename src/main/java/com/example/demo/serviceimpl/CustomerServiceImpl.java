package com.example.demo.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@EnableScheduling
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	/*@Scheduled(fixedRate = 600000)
    public void tarea3() {
		deleteRedis();
        System.out.println("Tarea de eliminación de REDIS ejecutada cada 10 minutos");
    }*/

    private long eventsLifeTime = 10;

	private static final String KEY = "Customer";

	@Autowired
	private RedisTemplate<String, Customer> redisTemplate;
	private HashOperations<String, String, Customer> hashOperations;

	public CustomerServiceImpl(RedisTemplate<String, Customer> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	//@Override
	//public Map<String, Customer> findAllRedis() {
	@Override
	public List<Customer> findAllRedis() {
		//hashOperations.entries(KEY)
		Set<String> keys = redisTemplate.keys("customer#*");
		List<Customer> customerList = new ArrayList<Customer>();
		for (String key : keys) {
			customerList.add(redisTemplate.opsForValue().get(key));
		}
		Collections.reverse(customerList);
		//Customer customernew2 = redisTemplate.opsForValue().get("customer#2");
		//return hashOperations.entries(KEY);
		return customerList;
	}

	@Override
	public Customer findByIdRedis(String id) {
		return (Customer) hashOperations.get(KEY, id);
	}

	@Override
	public void saveRedis(Customer customerNew) {
		//Sobrecarga de metodos y tiemp ode vida
		//hashOperations.put(KEY, UUID.randomUUID().toString(), customerNew);
		
		//final String key = String.format("user:%s",customerNew.getId());
		redisTemplate.opsForValue().set("customer#" + customerNew.getId(),customerNew);
		redisTemplate.expire("customer#" + customerNew.getId(), eventsLifeTime, TimeUnit.MINUTES );
	}

	@Override
	public void deleteRedis() {
		redisTemplate.delete(KEY);
		// hashOperations.delete(KEY, id);
	}

	@Override
	public List<Customer> findAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> findCustomerById(Long id) {
		return customerRepository.findById(id);
	}

	@Override
	public Customer saveCustomer(Customer customerNew) {
		if (customerNew != null) {
			return customerRepository.save(customerNew);
		}
		return new Customer();
	}

	@Override
	public String deleteCustomer(Long id) {
		deleteRedis();
		if (customerRepository.findById(id).isPresent()) {
			customerRepository.deleteById(id);
			return "{ \"status\": \"200\" }";
		}
		return "{ \"status\": \"404\" }";
	}

	@Override
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

	@Override
	public List<Customer> searchAllCustomers() throws JsonProcessingException {
		// Validar si REDIS posee informacion
		String astring = findAllRedis().toString();
		if (astring.equals("[]")) {
			System.out.println("-----------------------------------SQL-----------------------------------");
			// Insertar los valores de la funcion de JPA de findAll en una lista
			refreshRedisData(findAllCustomers());
			return findAllCustomers();
		} else {
			System.out.println("----------------------------------REDIS----------------------------------");
			/*
			// Creacion de un string plano para guardar el Map que trabaja por defecto REDIS
			// en un json,
			// se realiza para garantizar que la informacion enviada al back es la misma de
			// ambas fuentes
			String stringMap = new String();
			stringMap = "[ ";
			// Recuperar los valores de la consulta findAll() por defecto de REDIS
			Map<String, Customer> map = findAllRedis();
			// Recorrer el array de Maps y extraer los values para estructurar un string en
			// un json
			for (Map.Entry<String, Customer> entry : map.entrySet()) {
				stringMap = stringMap + "{ \"id\": " + "\"" + entry.getValue().getId() + "\"" + ", \"name\": " + "\""
						+ entry.getValue().getName() + "\"" + ", \"lastname\": " + "\"" + entry.getValue().getLastname()
						+ "\"" + ", \"phone\": " + "\"" + entry.getValue().getPhone() + "\" }, ";
			}
			stringMap = stringMap.substring(0, stringMap.length() - 2);
			stringMap = stringMap + " ]";

			// Mapear el string del json dentro de un List<Customer> para mantener un mismo
			// formato de informacion
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			List<Customer> customerList = Arrays.asList(mapper.readValue(stringMap, Customer[].class));
			// Sort de la lista de Customers puesto que los datos recuperados de REDIS
			// vienen en desorden
			customerList.sort(Comparator.comparing(Customer::getId));
			
			*/
			return findAllRedis();
		}
	}

	@Override
	public void refreshRedisData(List<Customer> customerList) {
		// Recorrer la lista 1 a 1 para hacer los inserts a REDIS
		for (Customer customer : customerList) {
			saveRedis(customer);
		}
	}

}
