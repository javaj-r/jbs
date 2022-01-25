package com.javid.console;

import com.javid.model.Account;
import com.javid.model.Transaction;
import com.javid.service.TransactionService;
import com.javid.util.Screen;

import java.sql.Date;
import java.util.List;

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
                    , "Select transaction"
                    , "Delete transaction"
                    , "Get account transactions by date");

            if (choice == 0)
                break;

            switch (choice) {
                case 1 -> selectTransaction();
                case 2 -> deleteTransaction();
                case 3 -> showAccountTransactions();
            }
        }
    }

    private void showAccountTransactions() {
        Account account = AccountConsole.getInstance().selectAccount("Select account to see transactions: ");
        Date startDate = Screen.getDate("Enter start date ", "Invalid date format");

        List<Transaction> transactions = transactionService.findAllByAccountIdAndStartDate(account, startDate);
        if (transactions.isEmpty()) {
            System.out.println("No transaction found!");
        }
        transactions.forEach(System.out::println);
    }

    private void selectTransaction() {
        Transaction transaction1 = selectTransaction("Select from menu: ");
        if (!transaction1.isNew()) {
            transaction = transaction1;
        }
    }

    private Transaction selectTransaction(String message) {
        List<Transaction> transactions = transactionService.findAll();
        String[] arr = transactions.stream().map(Transaction::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", ""
                , message, "Invalid choice"
                , "Cancel", arr);

        if (choice == 0) {
            return new Transaction();
        }
        return transactions.get(choice - 1);
    }

    private void deleteTransaction() {
        Transaction transaction1 = selectTransaction("Select transaction to delete: ");
        if (transaction1.isNew())
            return;
        transactionService.delete(transaction1);
    }

}
