package com.javid.console;

import com.javid.Application;
import com.javid.model.Branch;
import com.javid.model.Employee;
import com.javid.model.EmployeeRole;
import com.javid.service.EmployeeService;
import com.javid.util.Screen;

import java.util.List;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class EmployeeConsole {

    private Employee employee;
    private final EmployeeService employeeService;

    private static class Singleton {
        private static final EmployeeConsole INSTANCE = new EmployeeConsole();
    }

    public static EmployeeConsole getInstance() {
        return Singleton.INSTANCE;
    }

    private EmployeeConsole() {
        employeeService = new EmployeeService();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void mainMenu() {
        while (true) {
            int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                    , "Back to main"
                    , "Select employee"
                    , "Create employee"
                    , "Update employee"
                    , "Delete employee");

            if (choice == 0)
                break;

            switch (choice) {
                case 1 -> selectEmployee();
                case 2 -> createEmployee();
                case 3 -> updateEmployee();
                case 4 -> deleteEmployee();
            }
        }
    }

    public void selectEmployee() {
        Employee employee1 = selectEmployee("Select from menu: ");
        if (employee1.isNew())
            return;
        employee = employee1;
    }

    public Employee selectEmployee(String message) {
        List<Employee> employees = employeeService.findAll();
        String[] arr = employees.stream().map(Employee::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", ""
                , message, "Invalid choice"
                , "Cancel"
                , arr);

        if (choice == 0) {
            return new Employee();
        }
        return employees.get(choice - 1);
    }

    private void createEmployee() {
        String username = Screen.getString("Enter username: ");
        String password = Screen.getString("Enter password: ");

        EmployeeRole role = selectRole("Select from menu: ");

        System.out.println("Select manager.");
        selectEmployee();

        System.out.println("Select branch.");
        BranchConsole branchConsole = BranchConsole.getInstance();
        branchConsole.selectBranch();

        Employee employee1 = new Employee()
                .setUsername(username)
                .setPassword(password)
                .setRole(role)
                .setManager(this.employee)
                .setBranch(branchConsole.getBranch());
        employee1 = employeeService.create(employee1);
        if (!employee1.isNew()) {
            employee = employee1;
        }
    }

    private EmployeeRole selectRole(String message) {
        int choice = Screen.showMenu("Select employee role.", ""
                , message, "Invalid choice"
                , "Cancel", EmployeeRole.stringValues());

        if (choice == 0) {
            return null;
        }

        return EmployeeRole.values()[choice - 1];
    }

    private void updateEmployee() {
        Employee employee1 = selectEmployee("Select employee to update: ");
        if (employee1.isNew())
            return;

        while (true) {
            String username = Screen.getString("Enter - or new username: ").toLowerCase();
            if (!Application.isForUpdate(username))
                break;

            if (employeeService.findByUsername(new Employee().setUsername(username)) == null)
                employee1.setUsername(username.trim());

            System.out.println("Username already exists!");
        }

        String password = Screen.getPassword("Enter - or new password: ");
        if (Application.isForUpdate(password))
            employee1.setUsername(password.trim());

        EmployeeRole role = selectRole("Select new role from menu: ");
        if (role != null)
            employee1.setRole(role);

        this.employee = selectEmployee("Select new manager from menu: ");
        if (!employee.isNew())
            employee1.setManager(this.employee);

        BranchConsole branchConsole = BranchConsole.getInstance();
        Branch branch = branchConsole.selectBranch("Select new branch from menu: ");
        if (!branch.isNew())
            employee1.setBranch(branch);

        employeeService.update(employee1);
    }

    private void deleteEmployee() {
        Employee employee1 = selectEmployee("Select employee to delete: ");
        if (employee1.isNew())
            return;
        employeeService.delete(employee1);
    }

}
