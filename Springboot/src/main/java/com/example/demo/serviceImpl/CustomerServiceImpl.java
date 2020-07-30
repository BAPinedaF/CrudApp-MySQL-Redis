package com.example.demo.serviceImpl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@EnableScheduling
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	@Scheduled(fixedRate = 600000)
    public void tarea3() {
		deleteRedis();
        System.out.println("Tarea de eliminación de REDIS ejecutada cada 10 minutos");
    }

	private static final String KEY = "Customer";

	private RedisTemplate<String, Customer> redisTemplate;
	private HashOperations hashOperations;

	public CustomerServiceImpl(RedisTemplate<String, Customer> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public Map<String, Customer> findAllRedis() {
		return hashOperations.entries(KEY);
	}

	@Override
	public Customer findByIdRedis(String id) {
		return (Customer) hashOperations.get(KEY, id);
	}

	@Override
	public void saveRedis(Customer customerNew) {
		hashOperations.put(KEY, UUID.randomUUID().toString(), customerNew);
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
		Optional<Customer> customer = customerRepository.findById(id);
		return customer;
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
	public List<Customer> searchAllCustomers() throws JsonMappingException, JsonProcessingException {
		// Validar si REDIS posee informacion
		if (findAllRedis().toString() == "{}") {
			System.out.println("-----------------------------------SQL-----------------------------------");
			// Insertar los valores de la funcion de JPA de findAll en una lista
			refreshRedisData(findAllCustomers());
			return findAllCustomers();
		} else {
			System.out.println("----------------------------------REDIS----------------------------------");
			
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
			return customerList;
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
