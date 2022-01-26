package com.javid.repository;

import com.javid.model.Account;
import com.javid.model.Transaction;

import java.sql.Date;
import java.util.List;

/**
 * @author javid
 * Created on 1/17/2022
 */
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAllByAccountIdAndStartDate(Account account, Date startDate);

    List<Long> transfer(Transaction source, Transaction destination);
}
