package com.javid.database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HibernateSessionFactoryIT {

    @Test
    void getInstance() {
        assertNotNull(HibernateSessionFactory.getInstance());
    }
}