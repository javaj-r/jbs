package com.javid.repository.jdbc;

import com.javid.database.DatabaseConnection;
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
                SELECT id
                     , name
                     , manager_id
                     , bank_id
                FROM branch;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee branchManager = new Employee();
                branchManager.setId(resultSet.getLong("manager_id"));

                Bank bank = new Bank();
                bank.setId(resultSet.getLong("bank_id"));

                Branch branch = new Branch()
                        .setManager(branchManager)
                        .setBank(bank);
                branch.setId(resultSet.getLong("id"));
                branch.setName(resultSet.getString("name"));

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
                SELECT id
                     , name
                     , manager_id
                     , bank_id
                FROM branch
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Employee branchManager = new Employee();
                branchManager.setId(resultSet.getLong("manager_id"));

                Employee bankManager = new Employee();
                bankManager.setId(resultSet.getLong("bank_manager_id"));

                Bank bank = new Bank();
                bank.setId(resultSet.getLong("bank_id"));
                bank.setName(resultSet.getString("bank_name"));
                bank.setManager(bankManager);

                branch.setManager(branchManager)
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
                INSERT INTO branch(name, bank_id, manager_id)
                values (?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
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
                    manager_id=?
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

            statement.setLong(4, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
