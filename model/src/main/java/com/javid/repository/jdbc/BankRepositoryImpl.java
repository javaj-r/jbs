package com.javid.repository.jdbc;

import com.javid.database.DatabaseConnection;
import com.javid.model.Bank;
import com.javid.model.Branch;
import com.javid.model.Employee;
import com.javid.repository.BankRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/17/2022
 */
public class BankRepositoryImpl implements BankRepository {

    private Connection connection;

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Bank> findAll() {
        setConnection();
        List<Bank> banks = new ArrayList<>();
        String query = """
                SELECT b.id, b.name, b.manager_id, p.firstname, p.lastname, p.national_code
                FROM bank b
                         LEFT JOIN employee e on b.manager_id = e.id
                         LEFT JOIN person p on p.id = e.person_id;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee manager = new Employee();
                manager.setId(resultSet.getLong("manager_id"));
                manager.setFirstname(resultSet.getString("firstname"))
                        .setLastname(resultSet.getString("lastname"))
                        .setNationalCode(resultSet.getLong("national_code"));

                Bank bank = new Bank()
                        .setManager(manager);
                bank.setId(resultSet.getLong("id"));
                bank.setName(resultSet.getString("name"));

                banks.add(bank);
            }
            return banks;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return banks;
    }

    @Override
    public Bank findById(Long id) {
        setConnection();
        Bank bank = new Bank();
        String query = """
                SELECT b.id, b.name, b.manager_id, p.firstname, p.lastname, p.national_code
                FROM bank b
                         LEFT JOIN employee e on b.manager_id = e.id
                         LEFT JOIN person p on p.id = e.person_id
                WHERE b.id=?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Employee manager = new Employee();
                manager.setId(resultSet.getLong("manager_id"));
                manager.setFirstname(resultSet.getString("firstname"))
                        .setLastname(resultSet.getString("lastname"))
                        .setNationalCode(resultSet.getLong("national_code"));

                bank.setManager(manager);
                bank.setId(resultSet.getLong("id"));
                bank.setName(resultSet.getString("name"));
            }
            return bank;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bank;
    }

    @Override
    public Long save(Bank entity) {
        setConnection();
        String query = """
                INSERT INTO bank(name, manager_id)
                VALUES (?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            if (entity.getManager() == null || entity.getManager().isNew()) {
                statement.setNull(2, Types.BIGINT);
            } else {
                statement.setLong(2, entity.getManager().getId());
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
                DELETE FROM bank
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
    public void update(Bank entity) {
        setConnection();
        String query = """
                UPDATE bank
                SET name=?,
                    manager_id=?
                WHERE id=?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getName());
            if (entity.getManager() == null || entity.getManager().isNew()) {
                statement.setNull(2, Types.BIGINT);
            } else {
                statement.setLong(2, entity.getManager().getId());
            }
            statement.setLong(3, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
