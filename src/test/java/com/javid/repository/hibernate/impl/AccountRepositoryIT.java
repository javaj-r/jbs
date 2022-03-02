package com.javid.repository.hibernate.impl;

import com.javid.database.HibernateSessionFactory;
import com.javid.model.Account;
import com.javid.model.Customer;
import com.javid.repository.AccountRepository;
import com.javid.repository.CustomerRepository;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountRepositoryIT {

    SessionFactory sessionFactory;
    AccountRepository accountRepository;
    CustomerRepository customerRepository;

    Account account1;
    Account account2;
    Customer customer;


    @BeforeEach
    void setUp() {
        sessionFactory = HibernateSessionFactory.getInstance();
        accountRepository = new AccountRepositoryImpl(sessionFactory);
        customerRepository = new CustomerRepositoryImpl(sessionFactory);

        customer = new Customer()
                .setFirstname("f1")
                .setLastname("l1")
                .setNationalCode(1111L);

        account1 = new Account()
                .setBalance(1_000_000L)
                .setEnabled(true)
                .setCustomer(customer);

        account2 = new Account()
                .setBalance(2_000_000L)
                .setEnabled(false)
                .setCustomer(customer);
    }

    @AfterEach
    void tearDown() {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.createQuery("DELETE FROM Account ")
                        .executeUpdate();
                session.createQuery("DELETE FROM Customer ")
                        .executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Test
    void findAll() {
        customerRepository.save(customer);
        accountRepository.save(account1);
        accountRepository.save(account2);

        var accounts = accountRepository.findAll();

        assertEquals(2, accounts.size());
    }

    @Test
    void findById() {
        customerRepository.save(customer);
        accountRepository.save(account1);

        var actual = accountRepository.findById(account1.getId());

        assertNotNull(actual);
        assertEquals(account1.getBalance(), actual.getBalance());
    }

    @Test
    void save() {
        customerRepository.save(customer);
        accountRepository.save(account1);

        var actual = accountRepository.findById(account1.getId());

        assertNotNull(actual);
        assertAll(
                () -> assertEquals(account1.getBalance(), actual.getBalance()),
                () -> assertEquals(account1.isEnabled(), actual.isEnabled())
        );
    }

    @Test
    void deleteById() {
        customerRepository.save(customer);
        var id = accountRepository.save(account1);
        assertNotNull(accountRepository.findById(id));

        accountRepository.deleteById(id);

        assertNull(accountRepository.findById(id));
    }

    @Test
    void update() {
        customerRepository.save(customer);
        var id = accountRepository.save(account1);
        account2.setId(id);

        accountRepository.update(account2);

        var actual = accountRepository.findById(id);
        assertNotNull(actual);
        assertAll(
                () -> assertEquals(account2.getBalance(), actual.getBalance()),
                () -> assertEquals(account2.isEnabled(), actual.isEnabled())
        );
    }

    @Test
    void findAllByCustomerId() {
        var id = customerRepository.save(customer);

        accountRepository.save(account1);
        accountRepository.save(account2);

        var actual = accountRepository.findAllByCustomerId(id);

        assertEquals(2, actual.size());
    }
}