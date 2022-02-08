package com.javid.service;

import com.javid.model.Employee;
import com.javid.repository.EmployeeRepository;
import com.javid.repository.impl.EmployeeRepositoryImpl;

import java.util.List;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class EmployeeService {

    private final EmployeeRepository repository = new EmployeeRepositoryImpl();

    public List<Employee> findAll() {
        return repository.findAll();
    }

    public Employee create(Employee employee) {
        employee.setId(repository.save(employee));
        return employee;
    }

    public void update(Employee employee) {
        repository.update(employee);
    }

    public void delete(Employee employee) {
        if (!employee.isNew())
            repository.deleteById(employee.getId());
    }

    public Employee findByUsername(Employee employee) {
        return repository.findByUsername(employee);
    }
}
