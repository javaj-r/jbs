package com.javid.repository.hibernate.impl;

import com.javid.model.Account;
import com.javid.model.Customer;
import com.javid.repository.AccountRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryTest {

    @Mock
    SessionFactory sessionFactory;

    @Mock
    Session session;

    @Mock
    Query<Account> query;

    @Mock
    Transaction transaction;

    @Captor
    ArgumentCaptor<Long> longCaptor;

    @Captor
    ArgumentCaptor<String> stringCaptor;

    @Captor
    ArgumentCaptor<Account> accountCaptor;

    @Captor
    ArgumentCaptor<Class<Account>> accountTypeCaptor;

    AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        accountRepository = new AccountRepositoryImpl(sessionFactory);
        when(sessionFactory.openSession()).thenReturn(session);
    }

    @Test
    void findAll() {
        var account1 = new Account().setId(1L);
        var account2 = new Account().setId(2L);
        when(query.list()).thenReturn(List.of(account1, account2));
        when(session.createQuery("FROM Account", Account.class)).thenReturn(query);

        var actual = accountRepository.findAll();

        verify(sessionFactory).openSession();
        verify(session).createQuery("FROM Account", Account.class);
        verify(query).list();
        assertEquals(2, actual.size());
        assertArrayEquals(List.of(account1, account2).toArray(), actual.toArray());
    }

    @Test
    void findById() {
        var account = new Account().setId(1L);
        when(session.find(Account.class, 1L)).thenReturn(account);

        var actual = accountRepository.findById(1L);

        assertEquals(account, actual);
        verify(sessionFactory).openSession();
        verify(session).find(accountTypeCaptor.capture(), longCaptor.capture());
        assertEquals(Account.class, accountTypeCaptor.getValue());
        assertEquals(1L, longCaptor.getValue());
    }

    @Test
    void save() {
        var account = new Account();
        when(session.beginTransaction()).thenReturn(transaction);

        accountRepository.save(account);

        verify(sessionFactory).openSession();
        verify(session).beginTransaction();
        verify(transaction).commit();
        verify(session).save(accountCaptor.capture());
        assertEquals(account, accountCaptor.getValue());
    }

    @Test
    void deleteById() {
        when(session.beginTransaction()).thenReturn(transaction);
        when(session.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyLong())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        accountRepository.deleteById(1L);

        verify(sessionFactory).openSession();
        verify(session).beginTransaction();
        verify(transaction).commit();
        verify(session).createQuery(anyString());
        verify(query).setParameter("ID", 1L);
        verify(query).executeUpdate();
    }

    @Test
    void update() {
    }

    @Test
    void findAllByCustomerId() {
        var customer = new Customer().setId(1L);
        var accounts = List.of(new Account().setBalance(1L).setEnabled(true).setCustomer(customer),
        new Account().setBalance(2L).setEnabled(false).setCustomer(customer));

        when(session.createQuery("FROM Account a WHERE a.customer.id = :CustomerId", Account.class)).thenReturn(query);
        when(query.setParameter(anyString(), anyLong())).thenReturn(query);
        when(query.list()).thenReturn(accounts);

        var actual = accountRepository.findAllByCustomerId(1L);

        assertArrayEquals(accounts.toArray(), actual.toArray());
        verify(session).createQuery(stringCaptor.capture(), accountTypeCaptor.capture());
        assertEquals("FROM Account a WHERE a.customer.id = :CustomerId", stringCaptor.getValue());
        assertEquals(Account.class, accountTypeCaptor.getValue());

    }
}