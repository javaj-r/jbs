package com.javid.service;

import com.javid.model.Customer;
import com.javid.repository.CustomerRepository;
import com.javid.exception.ForeignKeyViolationException;
import com.javid.exception.NationalCodeValidationException;
import com.javid.repository.hibernate.impl.CustomerRepositoryImpl;

import java.util.List;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class CustomerService {

    private final CustomerRepository repository = new CustomerRepositoryImpl();
    private final AccountService accountService = new AccountService();

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Customer create(Customer customer) {
        validateNationalCode(customer.getNationalCode());
        if (repository.findByNationalCode(customer) == null) {
            customer.setId(repository.save(customer));
            return customer;
        } else {
            throw new NationalCodeValidationException("This national code already exists!");
        }
    }

    public void update(Customer customer) {
        validateNationalCode(customer.getNationalCode());
        Customer temp = repository.findByNationalCode(customer);
        if (temp == null || temp.getId().equals(customer.getId())) {
            repository.update(customer);
        } else {
            throw new NationalCodeValidationException("This national code is already exists for other customer!");
        }
    }

    public void delete(Customer customer) {
        validateCustomerDeletion(customer);
        repository.deleteById(customer.getId());
    }

    private void validateNationalCode(Long nationalCode) {
        if (nationalCode == null) {
            throw new NationalCodeValidationException("National code cannot be null");
        } else if (nationalCode < (long) Math.pow(10, 9) || nationalCode >= (long) Math.pow(10, 10)) {
            throw new NationalCodeValidationException("National code should be 10 digit number");
        }
    }

    private void validateCustomerDeletion(Customer customer) {
        if (customer == null) {
            throw new ForeignKeyViolationException("Customer is null");
        } else if (!accountService.findAllByCustomerId(customer).isEmpty()) {
            throw new ForeignKeyViolationException("Cannot remove customer(s) who have account(s)");
        }
    }

    public Customer findById(long id) {
        return repository.findById(id);
    }
}
