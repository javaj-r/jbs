package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author javid
 * Created on 1/15/2022
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class Account extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;

    private Long balance;
    private boolean enabled;

    @OneToMany
    @JoinColumn(name = "account_id")
    private Set<Transaction> transactions = new HashSet<>();

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
