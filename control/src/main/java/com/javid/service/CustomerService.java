package com.javid.service;

import com.javid.model.Customer;
import com.javid.repository.CustomerRepository;
import com.javid.repository.jdbc.CustomerRepositoryImpl;

import java.util.List;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class CustomerService {

    private final CustomerRepository repository = new CustomerRepositoryImpl();

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Customer create(Customer customer) {
        customer.setId(repository.save(customer));
        return customer;
    }

    public void delete(Customer customer) {
        if (!customer.isNew())
            repository.deleteById(customer.getId());
    }
}
