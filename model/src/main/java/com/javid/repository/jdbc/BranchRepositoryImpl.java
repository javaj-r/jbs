package com.javid.repository.jdbc;

import com.javid.database.DatabaseConnection;
import com.javid.model.Address;
import com.javid.model.Bank;
import com.javid.model.Branch;
import com.javid.model.Employee;
import com.javid.repository.BranchRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class BranchRepositoryImpl implements BranchRepository {

    private Connection connection;

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Branch> findAll() {
        setConnection();
        List<Branch> branches = new ArrayList<>();
        String query = """
                SELECT b.id
                     , b.name
                     , b.address_id
                     , a.country
                     , a.state
                     , a.city
                     , a.street
                     , a.postal_code
                     , b.manager_id
                     , b.bank_id
                     , b2.name       bank_name
                     , b2.manager_id bank_manager_id
                FROM branch b
                         LEFT JOIN address a on a.id = b.address_id
                         LEFT JOIN employee e on b.id = e.branch_id
                         LEFT JOIN bank b2 on b2.id = b.bank_id;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Address address = new Address()
                        .setCountry(resultSet.getString("country"))
                        .setState(resultSet.getString("state"))
                        .setCity(resultSet.getString("city"))
                        .setStreet(resultSet.getString("street"))
                        .setPostalCode(resultSet.getLong("postal_code"));
                address.setId(resultSet.getLong("address_id"));

                Employee branchManager = new Employee();
                branchManager.setId(resultSet.getLong("manager_id"));

                Employee bankManager = new Employee();
                bankManager.setId(resultSet.getLong("bank_manager_id"));

                Bank bank = new Bank();
                bank.setId(resultSet.getLong("bank_id"));
                bank.setName(resultSet.getString("bank_name"));
                bank.setManager(bankManager);

                Branch branch = new Branch()
                        .setAddress(address)
                        .setManager(branchManager)
                        .setBank(bank);
                branch.setId(resultSet.getLong("id"));
                branch.setName(resultSet.getString("nam"));

                branches.add(branch);
            }

            return branches;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return branches;
    }

    @Override
    public Branch findById(Long id) {
        setConnection();
        Branch branch = new Branch();
        String query = """
                SELECT b.id
                     , b.name
                     , b.address_id
                     , a.country
                     , a.state
                     , a.city
                     , a.street
                     , a.postal_code
                     , b.manager_id
                     , b.bank_id
                     , b2.name       bank_name
                     , b2.manager_id bank_manager_id
                FROM branch b
                         LEFT JOIN address a on a.id = b.address_id
                         LEFT JOIN employee e on b.id = e.branch_id
                         LEFT JOIN bank b2 on b2.id = b.bank_id
                WHERE b.id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Address address = new Address()
                        .setCountry(resultSet.getString("country"))
                        .setState(resultSet.getString("state"))
                        .setCity(resultSet.getString("city"))
                        .setStreet(resultSet.getString("street"))
                        .setPostalCode(resultSet.getLong("postal_code"));
                address.setId(resultSet.getLong("address_id"));

                Employee branchManager = new Employee();
                branchManager.setId(resultSet.getLong("manager_id"));

                Employee bankManager = new Employee();
                bankManager.setId(resultSet.getLong("bank_manager_id"));

                Bank bank = new Bank();
                bank.setId(resultSet.getLong("bank_id"));
                bank.setName(resultSet.getString("bank_name"));
                bank.setManager(bankManager);

                branch.setAddress(address)
                        .setManager(branchManager)
                        .setBank(bank);
                branch.setId(resultSet.getLong("id"));
                branch.setName(resultSet.getString("nam"));
            }

            return branch;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return branch;
    }

    @Override
    public Long save(Branch entity) {
        setConnection();
        String query = """
                INSERT INTO branch(name, bank_id)
                values (?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getBank().getId());
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
                DELETE FROM branch
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
    public void update(Branch entity) {
        setConnection();
        String query = """
                UPDATE branch
                SET name=?,
                    bank_id=?,
                    manager_id=?,
                    address_id=?
                WHERE id=?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getName());
            if (entity.getBank() == null || entity.getBank().isNew()) {
                statement.setNull(2, Types.BIGINT);
            } else {
                statement.setLong(2, entity.getBank().getId());
            }

            if (entity.getManager() == null || entity.getManager().isNew()) {
                statement.setNull(3, Types.BIGINT);
            } else {
                statement.setLong(3, entity.getManager().getId());
            }

            if (entity.getAddress() == null || entity.getAddress().isNew()) {
                statement.setNull(4, Types.BIGINT);
            } else {
                statement.setLong(4, entity.getAddress().getId());
            }

            statement.setLong(5, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
