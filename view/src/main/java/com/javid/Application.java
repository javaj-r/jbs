package com.javid;

import com.javid.console.*;
import com.javid.model.Bank;
import com.javid.service.BankService;
import com.javid.util.Screen;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class Application {

    public static void main(String[] args) {
//        createBank();

        mainMenu();

    }

    private static void createBank() {
        Bank bank = new BankService().create("National Bank");

        if (bank.isNew()) {
            System.out.println("Operation failed!!!");
            return;
        }

        System.out.println("Bank created successfully.");
        System.out.println("Name: " + bank.getName() + "\nId: " + bank.getId());
    }


    private static void mainMenu() {
        while (true) {
            int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                    , "Exit"
                    , "Bank"
                    , "Branch"
                    , "Employee"
                    , "Customer"
                    , "Account"
                    , "Card"
                    , "Transaction"
            );

            if (choice == 0)
                break;

            switch (choice) {
                case 1 -> BankConsole.getInstance().mainMenu();
                case 2 -> BranchConsole.getInstance().mainMenu();
                case 3 -> EmployeeConsole.getInstance().mainMenu();
                case 4 -> CustomerConsole.getInstance().mainMenu();
                case 5 -> AccountConsole.getInstance().mainMenu();
                case 6 -> CardConsole.getInstance().mainMenu();
                case 7 -> TransactionConsole.getInstance().mainMenu();
            }
        }
    }

    public static boolean isForUpdate(String element) {
        return "-".equals(element.trim());
    }
}
