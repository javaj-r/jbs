package com.javid.console;

import com.javid.Application;
import com.javid.model.Customer;
import com.javid.service.CustomerService;
import com.javid.service.ForeignKeyViolationException;
import com.javid.service.NationalCodeValidationException;
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

        if (!customer1.isNew()) {
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
        Customer customer1 = new Customer()
                .setFirstname(Screen.getString("Enter firstname: "))
                .setLastname(Screen.getString("Enter lastname: "));
        while (true) {
            try {
                customer1.setNationalCode(
                        Screen.getLong("Enter national cold: ", "Invalid number."));
                customer1 = customerService.create(customer1);
                break;
            } catch (NationalCodeValidationException e) {
                Screen.printError(e.getMessage(), 500);
            }
            int choice = Screen.showMenu("", ""
                    , "Select from menu: ", "Invalid choice."
                    , "Cancel", "Try another code");
            if (choice == 0)
                return;
        }
        if (!customer1.isNew()) {
            customer = customer1;
        }
    }

    private void updateCustomer() {
        Customer customer1 = selectCustomer("Select customer to update: ");
        if (customer1.isNew())
            return;

        String firstname = Screen.getString("Enter - or new firstname: ");
        if (Application.isForUpdate(firstname))
            customer1.setFirstname(firstname);

        String lastname = Screen.getString("Enter - or new lastname: ");
        if (Application.isForUpdate(lastname))
            customer1.setLastname(lastname);

        while (true) {
            try {
                long nationalCode = Screen.getLong("Enter 0 or new national cold: ", "Invalid number.");
                if (nationalCode != 0)
                    customer1.setNationalCode(nationalCode);
                customerService.update(customer1);
                break;
            } catch (NationalCodeValidationException e) {
                Screen.printError(e.getMessage(), 500);
            }
            int choice = Screen.showMenu("", ""
                    , "Select from menu: ", "Invalid choice."
                    , "Cancel", "Try another code");
            if (choice == 0)
                return;
        }
    }

    private void deleteCustomer() {
        Customer customer1 = selectCustomer("Select customer to delete: ");
        if (customer1.isNew())
            return;
        try {
            customerService.delete(customer1);
        } catch (ForeignKeyViolationException e) {
            Screen.printError(e.getMessage(), 500);
        }
    }
}
