package com.javid.repository.impl;

import com.javid.database.DatabaseConnection;
import com.javid.model.Branch;
import com.javid.model.Employee;
import com.javid.repository.BranchRepository;
import com.javid.repository.PrimitiveHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class BranchRepositoryImpl implements BranchRepository {

    private Connection connection;
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String MANAGER_ID = "manager_id";
    private static final String SELECT_QUERY = """
            SELECT id, name, manager_id
            FROM branch
            WHERE 1=1
            %s;""";


    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Branch> findAll() {
        setConnection();
        List<Branch> branches = new ArrayList<>();
        String query = SELECT_QUERY.formatted("ORDER BY id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                branches.add(parseBranch(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return branches;
    }

    @Override
    public Branch findById(Long id) {
        setConnection();
        String query = SELECT_QUERY.formatted("""
                AND id = ?
                ORDER BY id""");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseBranch(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Long save(Branch entity) {
        setConnection();
        String query = """
                INSERT INTO branch(name, manager_id)
                values (?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());

            PrimitiveHandler.setLong(statement, entity.getManager() == null || entity.getManager().isNew()
                    , 2, () -> entity.getManager().getId());

            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
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
                    manager_id=?
                WHERE id=?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getName());

            PrimitiveHandler.setLong(statement, entity.getManager() == null || entity.getManager().isNew()
                    , 2, () -> entity.getManager().getId());

            statement.setLong(3, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Branch parseBranch(ResultSet resultSet) throws SQLException {
        long managerId = resultSet.getLong(MANAGER_ID);
        Employee branchManager = new Employee()
                .setId(resultSet.wasNull() ? null : managerId);

        return new Branch()
                .setId(resultSet.getLong(ID))
                .setName(resultSet.getString(NAME))
                .setManager(branchManager);
    }
}
