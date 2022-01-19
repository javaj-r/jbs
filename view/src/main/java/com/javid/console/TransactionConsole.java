package com.javid.console;

import com.javid.model.Transaction;
import com.javid.service.TransactionService;
import com.javid.util.Screen;

/**
 * @author javid
 * Created on 1/19/2022
 */
public class TransactionConsole {

    private Transaction transaction;
    private TransactionService transactionService;

    private static class Singleton {
        private static final TransactionConsole INSTANCE = new TransactionConsole();
    }

    public static TransactionConsole getInstance() {
        return Singleton.INSTANCE;
    }

    private TransactionConsole() {
        transactionService = new TransactionService();
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
                case 1 -> selectTransaction();
                case 2 -> createTransaction();
                case 3 -> updateTransaction();
                case 4 -> deleteTransaction();
            }
        }
    }

    private void selectTransaction() {

    }

    private void createTransaction() {

    }

    private void updateTransaction() {

    }

    private void deleteTransaction() {

    }

}
