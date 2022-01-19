package com.javid.console;

import com.javid.Application;
import com.javid.model.Employee;
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
        List<Employee> employees = employeeService.findAll();
        String[] arr = employees.stream().map(Employee::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice"
                , "Cancel"
                , arr);

        if (choice > 0) {
            employee = employees.get(choice - 1);
        }
    }


    public Employee selectEmployee(String message) {
        List<Employee> employees = employeeService.findAll();
        String[] arr = employees.stream().map(Employee::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", "", message, "Invalid choice"
                , "Cancel"
                , arr);

        if (choice == 0) {
            return new Employee();
        }
        return employees.get(choice - 1);
    }

    private void createEmployee() {
        String firstname = Screen.getString("Enter firstname: ");
        String lastname = Screen.getString("Enter lastname: ");
        Long nationalCode = Screen.getLong("Enter national cold: ", "Invalid number.");
        String username = Screen.getString("Enter username: ");
        String password = Screen.getString("Enter password: ");

        System.out.println("Select manager.");
        selectEmployee();

        System.out.println("Select branch.");
        BranchConsole branchConsole = BranchConsole.getInstance();
        branchConsole.selectBranch();

        Employee employee1 = new Employee();
        employee1.setUsername(username)
                .setPassword(password)
                .setManager(this.employee)
                .setBranch(branchConsole.getBranch())
                .setFirstname(firstname)
                .setLastname(lastname)
                .setNationalCode(nationalCode);
        employee1 = employeeService.create(employee1);
        if (!employee1.isNew()) {
            employee = employee1;
        }
    }

    private void updateEmployee() {
        Employee employee1 = selectEmployee("Select employee to update");
        String firstname = Screen.getString("Enter - or new firstname: ");
        String lastname = Screen.getString("Enter - or new lastname: ");
        Long nationalCode = Screen.getLong("Enter 0 or new national code: ", "Invalid number.");
        String username = Screen.getString("Enter - or new username: ");
        String password = Screen.getPassword("Enter - or new password: ");

        if (Application.isForUpdate(firstname)) {
            employee1.setFirstname(firstname.trim());
        }
        if (Application.isForUpdate(lastname)) {
            employee1.setLastname(lastname.trim());
        }
        if (nationalCode > 0) {
            employee1.setNationalCode(nationalCode);
        }
        if (Application.isForUpdate(username)) {
            employee1.setUsername(username.trim());
        }
        if (Application.isForUpdate(password)) {
            employee1.setUsername(password.trim());
        }

        employeeService.update(employee1);

    }

    private void deleteEmployee() {
        Employee employee1 = selectEmployee("Select employee to delete: ");
        employeeService.delete(employee1);
    }

}
