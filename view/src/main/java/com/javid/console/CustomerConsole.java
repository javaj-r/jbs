package com.javid.console;

import com.javid.model.Customer;
import com.javid.service.CustomerService;
import com.javid.util.Screen;

import java.util.List;

/**
 * @author javid
 * Created on 1/19/2022
 */
public class CustomerConsole {

    private Customer customer;
    private final CustomerService customerService;

    private static class Singleton {
        private static final CustomerConsole INSTANCE = new CustomerConsole();
    }

    public static CustomerConsole getInstance() {
        return Singleton.INSTANCE;
    }

    private CustomerConsole() {
        customerService = new CustomerService();
    }

    public void mainMenu() {
        while (true) {
            int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                    , "Back to main"
                    , "Select customer"
                    , "Create customer"
                    , "Update customer"
                    , "Delete customer");

            if (choice == 0)
                break;

            switch (choice) {
                case 1 -> selectCustomer();
                case 2 -> createCustomer();
                case 3 -> updateCustomer();
                case 4 -> deleteCustomer();
            }
        }
    }

    public void selectCustomer() {
        Customer customer1 = selectCustomer("Select from menu: ");

        if (!customer.isNew()) {
            customer = customer1;
        }
    }

    public Customer selectCustomer(String message) {
        List<Customer> customers = customerService.findAll();
        String[] arr = customers.stream().map(Customer::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", "", message, "Invalid choice"
                , "Cancel"
                , arr);

        if (choice == 0) {
            return new Customer();
        }
        return customers.get(choice - 1);
    }

    private void createCustomer() {
        String firstname = Screen.getString("Enter firstname: ");
        String lastname = Screen.getString("Enter lastname: ");
        Long nationalCode = Screen.getLong("Enter national cold: ", "Invalid number.");

        Customer customer1 = new Customer();
        customer1.setFirstname(firstname)
                .setLastname(lastname)
                .setNationalCode(nationalCode);
        customer1 = customerService.create(customer1);
        if (!customer1.isNew()) {
            customer = customer1;
        }
    }

    private void updateCustomer() {

    }

    private void deleteCustomer() {
        Customer customer1 = selectCustomer("Select customer to delete: ");
        customerService.delete(customer1);
    }
}
