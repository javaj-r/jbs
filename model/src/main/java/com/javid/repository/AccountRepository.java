package com.javid.repository;

import com.javid.model.Account;

import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public interface AccountRepository extends CrudRepository<Account, Long> {

    List<Account> findAllByCustomerId(Long customerId);
}
