package com.javid.console;

import com.javid.model.Bank;
import com.javid.service.BankService;
import com.javid.util.Screen;

import java.util.List;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class BankConsole {

    private Bank bank;
    private final BankService bankService;

    private static class Singleton {
        private static final BankConsole INSTANCE = new BankConsole();
    }

    public static BankConsole getInstance() {
        return Singleton.INSTANCE;
    }

    private BankConsole() {
        bankService = new BankService();
        bank = bankService.getDefault();
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void mainMenu() {
        while (true) {
            int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                    , "Back to main"
                    , "Select bank"
                    , "Create bank"
                    , "Update bank"
                    , "Delete bank");

            if (choice == 0)
                break;

            switch (choice) {
                case 1 -> selectBank();
                case 2 -> createBank();
                case 3 -> updateBank();
                case 4 -> deleteBank();
            }
        }
    }

    public void selectBank() {
        List<Bank> banks = bankService.findAll();
        String[] arr = banks.stream().map(Bank::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice"
                , "Cancel"
                , arr);

        if (choice > 0) {
            bank = banks.get(choice - 1);
        }
    }

    public Bank selectBank(String message) {
        List<Bank> banks = bankService.findAll();
        String[] arr = banks.stream().map(Bank::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", "", message, "Invalid choice"
                , "Cancel"
                , arr);

        if (choice == 0) {
            return new Bank();
        }
        return banks.get(choice - 1);
    }

    private void createBank() {

    }

    private void updateBank() {

    }

    private void deleteBank() {

    }


}
