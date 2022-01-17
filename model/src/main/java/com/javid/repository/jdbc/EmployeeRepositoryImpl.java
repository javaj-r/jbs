package com.javid.repository.jdbc;

import com.javid.database.DatabaseConnection;
import com.javid.model.Branch;
import com.javid.model.Customer;
import com.javid.model.Employee;
import com.javid.repository.EmployeeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private Connection connection;

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Employee> findAll() {
        setConnection();
        List<Employee> employees = new ArrayList<>();
        String query = """
                SELECT e.id, p.firstname, p.lastname, p.national_code, e.branch_id, e.manager_id
                FROM employee e
                         LEFT JOIN person p on p.id = e.person_id;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Branch branch = new Branch();
                branch.setId(resultSet.getLong("branch_id"));

                Employee manager = new Employee();
                manager.setId(resultSet.getLong("manager_id"));

                Employee employee = new Employee()
                        .setBranch(branch)
                        .setManager(manager);
                employee.setFirstname(resultSet.getString("firstname"))
                        .setLastname(resultSet.getString("lastname"))
                        .setNationalCode(resultSet.getLong("national_code"));
                employee.setId(resultSet.getLong("id"));

                employees.add(employee);
            }
            return employees;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    @Override
    public Employee findById(Long id) {

        setConnection();
        Employee employee = new Employee();
        String query = """
                SELECT e.id, p.firstname, p.lastname, p.national_code, e.branch_id, e.manager_id
                FROM employee e
                         LEFT JOIN person p on p.id = e.person_id
                WHERE e.id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Branch branch = new Branch();
                branch.setId(resultSet.getLong("branch_id"));

                Employee manager = new Employee();
                manager.setId(resultSet.getLong("manager_id"));

                employee.setBranch(branch)
                        .setManager(manager);
                employee.setFirstname(resultSet.getString("firstname"))
                        .setLastname(resultSet.getString("lastname"))
                        .setNationalCode(resultSet.getLong("national_code"));
                employee.setId(resultSet.getLong("id"));
            }

            return employee;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employee;
    }

    @Override
    public Long save(Employee entity) {
        setConnection();
        String query = """
                WITH data(person_id) AS (
                    INSERT INTO person (firstname, lastname, national_code)
                        VALUES (?, ?, ?)
                        RETURNING id
                )
                INSERT
                INTO employee(person_id, branch_id, manager_id)
                SELECT d.person_id, ?, ?
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

            if (entity.getBranch() == null || entity.getBranch().isNew()) {
                statement.setNull(4, Types.BIGINT);
            } else {
                statement.setLong(4, entity.getBranch().getId());
            }

            if (entity.getManager() == null || entity.getManager().isNew()) {
                statement.setNull(5, Types.BIGINT);
            } else {
                statement.setLong(5, entity.getManager().getId());
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
                WHERE id=(SELECT e.person_id FROM employee e WHERE e.id = ?);
                                
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
                UPDATE person
                SET firstname=?,
                    lastname=?,
                    national_code=?
                WHERE id = (SELECT person_id FROM employee e WHERE e.id = ?);
                
                UPDATE employee
                SET branch_id=?,
                    manager_id=?
                WHERE id=?;
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

            if (entity.getBranch() == null || entity.getBranch().isNew()) {
                statement.setNull(5, Types.BIGINT);
            } else {
                statement.setLong(5, entity.getBranch().getId());
            }

            if (entity.getManager() == null || entity.getManager().isNew()) {
                statement.setNull(6, Types.BIGINT);
            } else {
                statement.setLong(6, entity.getManager().getId());
            }
            statement.setLong(7, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
