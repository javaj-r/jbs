package com.javid.repository;

import com.javid.model.Card;

import java.sql.Date;

/**
 * @author javid
 * Created on 1/16/2022
 */
public interface CardRepository extends CrudRepository<Card, Long> {

    Date getCurrentDate();

    Date getExpireDate(int year);

    Date getExpireDate(Date date);

    Card findByCardNumber(Long cardNumber);
}
