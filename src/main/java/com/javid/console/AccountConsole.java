package com.javid.console;

import com.javid.exception.AccountBalanceException;
import com.javid.model.Account;
import com.javid.model.Branch;
import com.javid.model.Customer;
import com.javid.service.AccountService;
import com.javid.service.CustomerService;
import com.javid.util.Screen;

import java.util.List;

/**
 * @author javid
 * Created on 1/19/2022
 */
public class AccountConsole {

    private Account account;
    private final AccountService accountService;
    private final CustomerService customerService;

    private static class Singleton {
        private static final AccountConsole INSTANCE = new AccountConsole();
    }

    public static AccountConsole getInstance() {
        return Singleton.INSTANCE;
    }

    private AccountConsole() {
        accountService = new AccountService();
        customerService = new CustomerService();
    }

    public void mainMenu() {
        while (true) {
            int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                    , "Back to main"
                    , "Select account"
                    , "Create account"
                    , "Update account"
                    , "Delete account"
                    , "Get customer accounts"
                    , "Get customer accounts fast");

            if (choice == 0)
                break;

            switch (choice) {
                case 1 -> selectAccount();
                case 2 -> createAccount();
                case 3 -> updateAccount();
                case 4 -> deleteAccount();
                case 5 -> showCustomerAccountsById();
                case 6 -> showCustomerAccounts();
            }
        }
    }

    private void showCustomerAccountsById() {
        Customer customer;
        while (true) {
            long id = Screen.getLong("Enter 0 or customer id to get accounts: ", "Invalid number");
            if (id == 0)
                return;
            customer = customerService.findById(id);
            if (customer != null)
                break;
            System.out.println("No customer with id " + id);
        }
        showCustomerAccounts(customer);
    }

    private void showCustomerAccounts() {
        Customer customer = CustomerConsole.getInstance().selectCustomer("Select customer: ");
        showCustomerAccounts(customer);
    }

    private void showCustomerAccounts(Customer customer) {
        if (customer == null || customer.isNew())
            return;

        List<Account> list = accountService.findAllByCustomerId(customer);
        if (list.isEmpty()) {
            System.out.println("No account found!");
        }
        list.forEach(System.out::println);
    }

    public void selectAccount() {
        Account account1 = selectAccount("Select from menu: ");

        if (!account1.isNew()) {
            account = account1;
        }
    }

    public Account selectAccount(String message) {
        List<Account> accounts = accountService.findAll();
        String[] arr = accounts.stream().map(Account::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", "", message, "Invalid choice"
                , "Cancel"
                , arr);

        if (choice == 0) {
            return new Account();
        }

        return accounts.get(choice - 1);
    }

    private void createAccount() {
        Customer customer = CustomerConsole.getInstance().selectCustomer("Select customer to add account: ");
        if (customer.isNew())
            return;

        Branch branch = BranchConsole.getInstance().selectBranch("Select branch: ");
        if (branch.isNew())
            return;

        Account account1 = new Account()
                .setEnabled(true)
                .setCustomer(customer)
                .setBranch(branch);

        int choice;
        do {
            try {
                account1.setBalance(
                        Screen.getLong("Enter start balance: ", "Invalid number."));
                account1 = accountService.create(account1);
                break;
            } catch (AccountBalanceException e) {
                Screen.printError(e.getMessage(), 2000);
            }
            choice = Screen.showMenu("", ""
                    , "Select from menu: ", "Invalid choice."
                    , "Cancel", "Try another balance");
        } while (choice != 0);

        if (!account1.isNew()) {
            account = account1;
        }
    }

    private void updateAccount() {
        Account account1 = selectAccount("Select account to update: ");
        if (account1.isNew())
            return;

        int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                , "Disable account.", "Enable account.");

        account1.setEnabled(choice != 0);

        BranchConsole branchConsole = BranchConsole.getInstance();
        Branch branch = branchConsole.selectBranch("Select new branch from menu: ");
        if (!branch.isNew())
            account1.setBranch(branch);

        do {
            try {
                account1.setBalance(
                        Screen.getLong("Enter 0 or new balance: ", "Invalid number."));
                accountService.update(account1);
                break;
            } catch (AccountBalanceException e) {
                Screen.printError(e.getMessage(), 2000);
            }
            choice = Screen.showMenu("", ""
                    , "Select from menu: ", "Invalid choice."
                    , "Cancel", "Try another balance");
        } while (choice != 0);
    }

    private void deleteAccount() {
        Account account1 = selectAccount("Select account to delete: ");
        if (account1.isNew())
            return;
        accountService.delete(account1);
    }
}
