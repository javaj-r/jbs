package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author javid
 * Created on 1/15/2022
 */
@Getter
@Setter
@Accessors(chain = true)
public class Account extends BaseEntity {

    private Branch branch;
    private Customer customer;
    private Card card;
    private Long balance;
    private boolean enabled;
    private Set<Transaction> transactions;

    @Override
    public Account setId(Long id) {
        super.setId(id);
        return this;
    }

    @Override
    public String toString() {
        return "{ " +
                "id=" + getId() +
                ", branchId=" + (branch == null ? "'no branch'": branch.getId()) +
                ", customerId=" + (customer == null? "'no customer'" : customer.getId()) +
                ", cardId=" + (card == null? "'no card'" : card.getId()) +
                ", balance=" + balance +
                ", enabled=" + enabled +
                " }";
    }
}
