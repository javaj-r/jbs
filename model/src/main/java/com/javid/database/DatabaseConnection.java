package com.javid.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class DatabaseConnection {
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            setConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static class Singleton {
        private static final DatabaseConnection INSTANCE = new DatabaseConnection();
    }

    public static DatabaseConnection getInstance() {
        return Singleton.INSTANCE;
    }


    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                setConnection();
            }
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bank_system", "postgres", "123");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }}
