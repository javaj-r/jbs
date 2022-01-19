package com.javid.service;

import com.javid.model.Account;
import com.javid.model.Customer;
import com.javid.repository.AccountRepository;
import com.javid.repository.jdbc.AccountRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author javid
 * Created on 1/19/2022
 */
public class AccountService {

    private final AccountRepository repository = new AccountRepositoryImpl();

    public List<Account> findAll(Customer customer) {
        if (customer.isNew())
            return new ArrayList<>();

        return repository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .filter(account -> account.getCustomer() != null)
                .filter(account -> customer.getId().equals(account.getCustomer().getId()))
                .toList();
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account create(Account account) {
        account.setId(repository.save(account));
        return account;
    }

    public void update(Account account) {
        repository.update(account);
    }

    public void delete(Account account) {
        repository.deleteById(account.getId());
    }

    public Account findById(Long id) {
        return repository.findById(id);
    }

}
