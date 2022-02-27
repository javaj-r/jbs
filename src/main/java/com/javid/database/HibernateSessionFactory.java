package com.javid.database;

import com.javid.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateSessionFactory {

    private static class LazyHolder {
        static final SessionFactory INSTANCE;

        static {
            var registry = new StandardServiceRegistryBuilder()
                    .configure()
                    .build();

            INSTANCE = new MetadataSources(registry)
                    .addAnnotatedClass(BaseEntity.class)
                    .addAnnotatedClass(Branch.class)
                    .addAnnotatedClass(Employee.class)
                    .addAnnotatedClass(Customer.class)
                    .addAnnotatedClass(Account.class)
                    .addAnnotatedClass(Card.class)
                    .addAnnotatedClass(Transaction.class)
                    .buildMetadata()
                    .buildSessionFactory();
        }
    }

    public static SessionFactory getInstance() {
        return LazyHolder.INSTANCE;
    }
}
