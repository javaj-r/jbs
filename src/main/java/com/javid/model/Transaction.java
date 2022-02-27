package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.sql.Date;
import java.sql.Time;

/**
 * @author javid
 * Created on 1/15/2022
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class Transaction extends BaseEntity {

    @ManyToOne
    private Account account;

    private Long amount;
    private Time time;
    private Date date;
    private TransactionType type;
    private TransactionStatus status;

    @Override
    public Transaction setId(Long id) {
        super.setId(id);
        return this;
    }

    @Override
    public String toString() {
        return "{ " +
               "id=" + getId() +
               ", amount=" + amount +
               ", accountId=" + (account == null ? "'no account'" : account.getId()) +
               ", time=" + time +
               ", date=" + date +
               ", type=" + (type == null ? null : type.name()) +
               ", status=" + (status == null ? null : status.name()) +
               " }";
    }
}
