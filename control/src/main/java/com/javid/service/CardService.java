package com.javid.service;

import com.javid.model.Account;
import com.javid.model.Card;
import com.javid.repository.CardRepository;
import com.javid.repository.jdbc.CardRepositoryImpl;

import java.util.List;
import java.util.Random;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class CardService {

    private final CardRepository repository = new CardRepositoryImpl();
    private final AccountService accountService = new AccountService();

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
}
