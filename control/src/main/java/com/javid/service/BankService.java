package com.javid.service;

import com.javid.model.Bank;
import com.javid.repository.BankRepository;
import com.javid.repository.jdbc.BankRepositoryImpl;

import java.util.List;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class BankService {

    private BankRepository repository = new BankRepositoryImpl();

    public Bank create(String name) {
        Bank bank = new Bank();
        bank.setName(name);
        bank.setId(repository.save(bank));
        return bank;
    }

    public List<Bank> findAll() {
        return repository.findAll();
    }

    public Bank getDefault() {
        List<Bank> banks = repository.findAll();
        if (banks.isEmpty()) {
            return null;
        }
        return banks.get(0);
    }

}
