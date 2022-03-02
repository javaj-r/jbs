package com.javid.repository.hibernate.impl;

import com.javid.model.Account;
import com.javid.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */

@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final SessionFactory sessionFactory;

    @Override
    public List<Account> findAll() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM Account", Account.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Account findById(Long id) {
        try (var session = sessionFactory.openSession()) {
            return session.find(Account.class, id);
        }
    }

    @Override
    public Long save(Account entity) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.save(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }

        return entity.getId();
    }

    @Override
    public void deleteById(Long id) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.createQuery("DELETE FROM Account a WHERE a.id = :ID")
                        .setParameter("ID", id)
                        .executeUpdate();

                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Account entity) {
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.update(entity);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Account> findAllByCustomerId(Long customerId) {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM Account a WHERE a.customer.id = :CustomerId", Account.class)
                    .setParameter("CustomerId", customerId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
