package com.javid.repository.jdbc;

import com.javid.database.DatabaseConnection;
import com.javid.model.Customer;
import com.javid.repository.CustomerRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class CustomerRepositoryImpl implements CustomerRepository {

    private Connection connection;

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Customer> findAll() {
        setConnection();
        List<Customer> customers = new ArrayList<>();
        String query = """
                SELECT c.id, p.firstname, p.lastname, p.national_code
                FROM customer c
                         LEFT JOIN person p on p.id = c.person_id;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getLong("id"));
                customer.setFirstname(resultSet.getString("firstname"))
                        .setFirstname(resultSet.getString("lastname"))
                        .setNationalCode(resultSet.getLong("national_code"));

                customers.add(customer);
            }
            return customers;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    @Override
    public Customer findById(Long id) {
        setConnection();
        Customer customer = new Customer();
        String query = """
                SELECT c.id, p.firstname, p.lastname, p.national_code
                FROM customer c
                         LEFT JOIN person p on p.id = c.person_id
                WHERE c.id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                customer.setId(resultSet.getLong("id"));
                customer.setFirstname(resultSet.getString("firstname"))
                        .setFirstname(resultSet.getString("lastname"))
                        .setNationalCode(resultSet.getLong("national_code"));
            }

            return customer;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }

    @Override
    public Long save(Customer entity) {
        setConnection();
        String query = """
                WITH data(person_id) AS (
                    INSERT INTO person(firstname, lastname, national_code)
                    VALUES (?, ?, ?)
                    RETURNING id
                )
                INSERT
                INTO customer (person_id)
                SELECT d.person_id
                FROM data d;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFirstname());
            statement.setString(2, entity.getLastname());
            if (entity.getNationalCode() == null) {
                statement.setNull(3, Types.BIGINT);
            } else {
                statement.setLong(3, entity.getNationalCode());
            }
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteById(Long id) {
        setConnection();
        String query = """
                DELETE
                FROM person
                WHERE id=(SELECT c.person_id FROM customer c WHERE c.id = ?);
                
                DELETE
                FROM customer
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Customer entity) {
        setConnection();
        String query = """
                UPDATE person
                SET firstname=?,
                    lastname=?,
                    national_code=?
                WHERE id = (SELECT person_id FROM customer c WHERE c.id = ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getFirstname());
            statement.setString(2, entity.getLastname());
            if (entity.getNationalCode() == null) {
                statement.setNull(3, Types.BIGINT);
            } else {
                statement.setLong(3, entity.getNationalCode());
            }
            statement.setLong(4, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
