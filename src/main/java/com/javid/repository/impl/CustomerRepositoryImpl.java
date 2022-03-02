package com.javid.repository.impl;

import com.javid.database.DatabaseConnection;
import com.javid.model.Customer;
import com.javid.repository.CustomerRepository;
import com.javid.repository.PrimitiveHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class CustomerRepositoryImpl implements CustomerRepository {

    private Connection connection;
    private static final String ID = "id";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String NATIONAL_CODE = "national_code";
    private static final String SELECT_QUERY = "SELECT id, firstname, lastname, national_code"
            + "\n FROM customer"
            + "\n WHERE 1=1"
            + "\n %s;";

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Customer> findAll() {
        setConnection();
        List<Customer> customers = new ArrayList<>();
        String query = String.format(SELECT_QUERY, "ORDER BY id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                customers.add(parseCustomer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    @Override
    public Customer findById(Long id) {
        setConnection();
        String query = String.format(SELECT_QUERY,"AND id = ?" + "\n ORDER BY id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseCustomer(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Long save(Customer entity) {
        setConnection();
        String query = "INSERT INTO customer (firstname, lastname, national_code)"
                + "\n VALUES (?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFirstname());
            statement.setString(2, entity.getLastname());
            PrimitiveHandler.setLong(statement, entity.getNationalCode() == null, 3, entity::getNationalCode);
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(ID);
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
        String query = "DELETE"
                + "\n FROM customer"
                + "\n WHERE id = ?";
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
        String query = "UPDATE customer"
                + "\n SET firstname=?,"
                + "\n     lastname=?,"
                + "\n     national_code=?"
                + "\n WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getFirstname());
            statement.setString(2, entity.getLastname());
            PrimitiveHandler.setLong(statement, entity.getNationalCode() == null, 3, entity::getNationalCode);
            statement.setLong(4, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer findByNationalCode(Customer entity) {
        setConnection();
        String query = String.format(SELECT_QUERY, "AND national_code = ?" + "\n ORDER BY id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, entity.getNationalCode());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseCustomer(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Customer parseCustomer(ResultSet resultSet) throws SQLException {
        return new Customer().setId(resultSet.getLong(ID))
                .setFirstname(resultSet.getString(FIRSTNAME))
                .setLastname(resultSet.getString(LASTNAME))
                .setNationalCode(resultSet.getLong(NATIONAL_CODE));
    }

}
