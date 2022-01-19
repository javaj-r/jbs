package com.javid.console;

import com.javid.model.Account;
import com.javid.model.Branch;
import com.javid.model.Customer;
import com.javid.service.AccountService;
import com.javid.util.Screen;

import java.util.List;

/**
 * @author javid
 * Created on 1/19/2022
 */
public class AccountConsole {

    private Account account;
    private final AccountService accountService;

    private static class Singleton {
        private static final AccountConsole INSTANCE = new AccountConsole();
    }

    public static AccountConsole getInstance() {
        return Singleton.INSTANCE;
    }

    private AccountConsole() {
        accountService = new AccountService();
    }

    public void mainMenu() {
        while (true) {
            int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                    , "Back to main"
                    , "Select branch"
                    , "Create branch"
                    , "Update branch"
                    , "Delete branch"
                    , "Get customer accounts");

            if (choice == 0)
                break;

            switch (choice) {
                case 1 -> selectAccount();
                case 2 -> createAccount();
                case 3 -> updateAccount();
                case 4 -> deleteAccount();
                case 5 -> showCustomerAccounts();

            }
        }
    }

    private void showCustomerAccounts() {
        Customer customer = CustomerConsole.getInstance().selectCustomer("Select customer: ");
        if (customer.isNew())
            return;

        List<Account> list = accountService.findAll(customer);
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

        Long balance = Screen.getLong("Enter start balance: ", "Invalid number.");
        if (balance < 0) {
            Screen.printError("Balance cannot be negative!");
            return;
        }

        Account account1 = new Account()
                .setEnabled(true)
                .setCustomer(customer)
                .setBranch(branch)
                .setBalance(balance);

        account1 = accountService.create(account1);
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
        accountService.update(account1);
    }

    private void deleteAccount() {
        Account account1 = selectAccount("Select account to delete: ");
        if (account1.isNew())
            return;
        accountService.delete(account1);
    }
}
