package com.javid.console;

import com.javid.model.Account;
import com.javid.model.Card;
import com.javid.service.CardService;
import com.javid.util.Screen;

import java.util.List;

/**
 * @author javid
 * Created on 1/19/2022
 */
public class CardConsole {

    private Card card;
    private final CardService cardService;

    private static class Singleton {
        private static final CardConsole INSTANCE = new CardConsole();
    }

    public static CardConsole getInstance() {
        return Singleton.INSTANCE;
    }

    private CardConsole() {
        cardService = new CardService();
    }

    public void mainMenu() {
        while (true) {
            int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                    , "Back to main"
                    , "Select card"
                    , "Create card"
                    , "Update card"
                    , "Delete card"
                    , "Set or change password");

            if (choice == 0)
                break;

            switch (choice) {
                case 1 -> selectCard();
                case 2 -> createCard();
                case 3 -> updateCard();
                case 4 -> deleteCard();
                case 5 -> changePassword();
            }
        }
    }

    private void changePassword() {
        Card card1 = selectCard("Select card to set or change password: ");
        if (card1.isNew())
            return;
        if (card1.getPassword1() == null || card1.getPassword1() < 1000) {
            int pass1 = getPassword1();
            if (pass1 == 0)
                return;
            card1.setPassword1(pass1);
            cardService.update(card1);
        }

        int choice = Screen.showMenu("", "", "Select from menu: ", "Invalid choice."
                , "Cancel", "Change password 1", "Change password 2");

        if (choice == 0)
            return;

        if (testPassword1(card1)) {
            if (choice == 1) {
                int pass = getPassword1();
                if (pass == 0)
                    return;
                card1.setPassword1(pass);
                cardService.update(card1);
            } else if (choice == 2) {
                int pass2 = Screen.getInt("Enter a numeric password or 0 to cancel: ", "Invalid number.");
                if (pass2 == 0)
                    return;
                card1.setPassword2(pass2);
                cardService.update(card1);
            }
        }
    }

    private void selectCard() {
        Card card1 = selectCard("Select from menu: ");

        if (!card1.isNew()) {
            card = card1;
        }
    }

    private Card selectCard(String message) {
        List<Card> cards = cardService.findAll();
        String[] arr = cards.stream().map(Card::toString)
                .toList()
                .toArray(new String[0]);

        int choice = Screen.showMenu("", "", message, "Invalid choice"
                , "Cancel"
                , arr);

        if (choice == 0) {
            return new Card();
        }

        return cards.get(choice - 1);
    }

    private void createCard() {
        Account account = AccountConsole.getInstance().selectAccount("Select card account: ");
        if (account == null || account.isNew())
            return;
        if (account.getCard() != null || !account.getCard().isNew()) {
            Screen.printError("This account already has a card!");
        }

        Card card1 = new Card()
                .setAccount(account);
        card1 = cardService.create(card1);
        if (!card1.isNew()) {
            card = card1;
        }
    }

    private void updateCard() {
        Card card1 = selectCard("Select card to update: ");
        if (card1.isNew())
            return;

        int choice = Screen.showMenu("", "",
                "Select from menu: ", "Invalid choice."
                , "Disable card.", "Enable card.");

        card1.setEnabled(choice != 0);
        cardService.update(card1);
    }

    private void deleteCard() {
        Card card1 = selectCard("Select card to delete: ");
        if (card1.isNew())
            return;

        cardService.delete(card1);
    }

    private int getPassword1() {
        while (true) {
            int password = Screen.getInt("Enter a 4 digit password or 0 to cancel: ", "Invalid number.");
            if (password == 0 || (password >= 1000 && password < 10000))
                return password;
        }
    }

    private boolean testPassword1(Card card) {
        int counter = 0;
        while (true) {
            int password = Screen.getInt("Enter password or 0 to cancel: ", "Invalid number.");
            if (password == 0)
                return false;

            if (password == card.getPassword1())
                return true;

            if (++counter == 3) {
                cardService.update(card.setEnabled(false));
                return false;
            }
        }
    }

    public void transferMoneyCardToCard() {

    }

}
