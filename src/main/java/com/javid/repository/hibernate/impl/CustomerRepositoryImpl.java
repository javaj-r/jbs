package com.javid.repository.hibernate.impl;

import com.javid.database.HibernateSessionFactory;
import com.javid.model.Customer;
import com.javid.repository.CustomerRepository;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class CustomerRepositoryImpl implements CustomerRepository {

    private final SessionFactory sessionFactory = HibernateSessionFactory.getInstance();

    @Override
    public List<Customer> findAll() {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM Customer", Customer.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Customer findById(Long id) {
        try (var session = sessionFactory.openSession()) {
            return session.find(Customer.class, id);
        }
    }

    @Override
    public Long save(Customer entity) {
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
                session.createQuery("DELETE FROM Customer a WHERE a.id = :ID")
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
    public void update(Customer entity) {
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
    public Customer findByNationalCode(Customer entity) {
        try (var session = sessionFactory.openSession()) {
            return session.createQuery("FROM Customer c WHERE c.nationalCode = :NationalCode", Customer.class)
                    .setParameter("NationalCode", entity.getNationalCode())
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
