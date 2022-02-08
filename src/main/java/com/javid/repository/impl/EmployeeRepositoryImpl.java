package com.javid.repository.impl;

import com.javid.database.DatabaseConnection;
import com.javid.model.Branch;
import com.javid.model.Employee;
import com.javid.model.EmployeeRole;
import com.javid.repository.PrimitiveHandler;
import com.javid.repository.EmployeeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private Connection connection;
    private static final String ID = "id";
    private static final String ROLE = "role";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String BRANCH_ID = "branch_id";
    private static final String MANAGER_ID = "manager_id";
    private static final String SELECT_QUERY = """
            SELECT id, username, password, branch_id, manager_id, role
            FROM employee
            WHERE 1=1
            %s;""";

    private void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Employee> findAll() {
        setConnection();
        List<Employee> employees = new ArrayList<>();
        String query = SELECT_QUERY.formatted("ORDER BY id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                employees.add(parseEmployee(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    @Override
    public Employee findById(Long id) {
        setConnection();
        String query = SELECT_QUERY.formatted("""
                AND id = ?
                ORDER BY id""");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseEmployee(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Long save(Employee entity) {
        setConnection();
        String query = """
                INSERT
                INTO employee(username, password, branch_id, manager_id, role)
                VALUES (?, ?, ?, ?, ?::employee_role);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            PrimitiveHandler.setLong(statement, entity.getBranch() == null || entity.getBranch().isNew()
                    , 3, () -> entity.getBranch().getId());
            PrimitiveHandler.setLong(statement, entity.getManager() == null || entity.getManager().isNew()
                    , 4, () -> entity.getManager().getId());
            statement.setString(5, entity.getRole() == null ? null : entity.getRole().name());

            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(ID);
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
                DELETE
                FROM employee
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
    public void update(Employee entity) {
        setConnection();
        String query = """
                UPDATE employee
                SET username=?,
                    password=?,
                    branch_id=?,
                    manager_id=?,
                    role=?::employee_role
                WHERE id=?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            PrimitiveHandler.setLong(statement, entity.getBranch() == null || entity.getBranch().isNew()
                    , 3, () -> entity.getBranch().getId());
            PrimitiveHandler.setLong(statement, entity.getManager() == null || entity.getManager().isNew()
                    , 4, () -> entity.getManager().getId());
            statement.setString(5, entity.getRole() == null ? null : entity.getRole().name());
            statement.setLong(6, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Employee findByUsername(Employee entity) {
        setConnection();
        String query = SELECT_QUERY.formatted("AND username = ?");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getUsername());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseEmployee(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Employee findByUsernamePassword(Employee entity) {
        setConnection();
        String query = SELECT_QUERY.formatted("""
                AND username = ?
                AND password = ?""");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseEmployee(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Employee parseEmployee(ResultSet resultSet) throws SQLException {
        long tempId = resultSet.getLong(BRANCH_ID);
        Branch branch = new Branch()
                .setId(resultSet.wasNull() ? null : tempId);

        tempId = resultSet.getLong(MANAGER_ID);
        Employee manager = new Employee()
                .setId(resultSet.wasNull() ? null : tempId);

        String role = resultSet.getString(ROLE);

        return new Employee().setId(resultSet.getLong(ID))
                .setUsername(resultSet.getString(USERNAME))
                .setPassword(resultSet.getString(PASSWORD))
                .setRole(role == null ? null : EmployeeRole.valueOf(role))
                .setBranch(branch)
                .setManager(manager);
    }
}
