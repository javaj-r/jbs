package com.javid.service;

import com.javid.model.Account;
import com.javid.model.Transaction;
import com.javid.model.TransactionStatus;
import com.javid.model.TransactionType;
import com.javid.repository.TransactionRepository;
import com.javid.repository.jdbc.TransactionRepositoryImpl;
import com.javid.exception.AccountBalanceException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/18/2022
 */
public class TransactionService {

    private final TransactionRepository repository = new TransactionRepositoryImpl();

    public List<Transaction> findAll() {
        return repository.findAll();
    }

    public List<Transaction> findAllByAccountIdAndStartDate(Account account, Date startDate) {
        return repository.findAllByAccountIdAndStartDate(account, startDate);
    }

    public void delete(Transaction transaction) {
        repository.deleteById(transaction.getId());
    }

    public List<Transaction> transfer(Transaction source, Transaction destination, long amount) {
        long fee = 600L;
        validateWithdraw(source.getAccount().getBalance(), amount, fee);
        amount = amount < 0 ? amount * -1 : amount;
        destination.setAmount(amount)
                .setType(TransactionType.DEPOSIT)
                .setStatus(TransactionStatus.ACCEPTED);

        source.setAmount((amount * -1) - fee)
                .setType(TransactionType.WITHDREW)
                .setStatus(TransactionStatus.ACCEPTED);

        List<Long> idList = repository.transfer(source, destination);
        List<Transaction> transactions = new ArrayList<>();
        idList.forEach(id -> transactions.add(repository.findById(id)));

        return transactions;
    }

    private void validateWithdraw(Long balance, Long amount, Long fee) {
        if (amount == null) {
            throw new AccountBalanceException("Transaction amount cannot be null");
        }
        long balance1 = balance;
        long amount1 = amount < 0 ? amount * -1 : amount;
        if (balance1 - amount1 - fee < 0) {
            throw new AccountBalanceException("Low balance");
        }
    }
}
