package com.javid.service;

import com.javid.model.Account;
import com.javid.model.Card;
import com.javid.model.Transaction;
import com.javid.repository.CardRepository;
import com.javid.repository.jdbc.CardRepositoryImpl;

import java.sql.Date;
import java.util.List;
import java.util.Random;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class CardService {

    private final CardRepository repository = new CardRepositoryImpl();
    private final AccountService accountService = new AccountService();
    private final TransactionService transactionService = new TransactionService();

    public List<Card> findAll() {
        return repository.findAll();
    }

    public Card create(Card card) {
        if (card.getAccount() == null || card.getAccount().isNew())
            return null;

        Account account = accountService.findById(card.getAccount().getId());
        if (account.isNew())
            return null;

        Long number = Long.parseLong("" + ((account.getBranch().getId() + 1000) % 10000)
                                     + ((account.getCustomer().getId() + 10000000) % 100000000)
                                     + ((account.getId() + 1000) % 10000));

        Random random = new Random();
        card.setCvv2(random.nextInt(100, 1000))
                .setExpireDate(repository.getExpireDate(3))
                .setNumber(number)
                .setEnabled(true);

        card.setId(repository.save(card));

        account.setCard(card);
        accountService.update(account);
        return card;
    }

    public void update(Card card) {
        repository.update(card);
    }

    public void delete(Card card) {
        repository.deleteById(card.getId());
    }

    public Card findByCardNumber(Card card) {
        validateCardNumber(card.getNumber());
        Card foundCard = repository.findByCardNumber(card.getNumber());
        if (foundCard != null)
            return foundCard;
        throw new CardNumberException("Wrong card number");
    }

    private void validateCardNumber(Long cardNumber) {
        if (cardNumber == null) {
            throw new CardNumberException("Card number cannot be null");
        } else if (cardNumber < (long) Math.pow(10, 15) || cardNumber >= (long) Math.pow(10, 16)) {
            throw new CardNumberException("Invalid card number");
        }
    }

    public void transferMoneyCardToCard(Card source, Card destination, long amount) {

        Card card1 = findByCardNumber(source);
        Card card2 = findByCardNumber(destination);
        authorizeCard(source, card1);

        Account account1 = accountService.findById(card1.getAccount().getId());
        Account account2 = accountService.findById(card2.getAccount().getId());
        transactionService.transfer(new Transaction().setAccount(account1)
                , new Transaction().setAccount(account2)
                , amount
        );

    }

    private void authorizeCard(Card source, Card fetched) {
        Date currentDate = repository.getCurrentDate();
        source.setExpireDate(repository.getExpireDate(source.getExpireDate()));
        if (!source.getPassword2().equals(fetched.getPassword2())) {
            throw new AuthenticationException("Wrong password!");
        }
        if (!fetched.isEnabled()) {
            throw new AuthenticationException("Your card is disabled!");
        }
        if (source.getExpireDate().compareTo(fetched.getExpireDate()) != 0) {
            throw new AuthenticationException("Wrong expire date!");
        }
        if (source.getExpireDate().compareTo(currentDate) < 0) {
            throw new AuthenticationException("Your card is expired!");
        }
        if (!source.getCvv2().equals(fetched.getCvv2())) {
            throw new AuthenticationException("Wrong CVV2!");
        }
    }
}
