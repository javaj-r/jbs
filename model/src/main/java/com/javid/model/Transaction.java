package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author javid
 * Created on 1/15/2022
 */
@Getter
@Setter
@Accessors(chain = true)
public class Transaction extends BaseEntity {

    private Long amount;
    private Account account;
    private TransactionType type;
    private TransactionStatus status;
}
