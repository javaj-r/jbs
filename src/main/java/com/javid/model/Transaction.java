package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
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
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    private Long amount;

    @Column(name = "t_time")
    private Time time;

    @Column(name = "t_date")
    private Date date;

    @Column(name = "t_type")
    private TransactionType type;

    @Column(name = "t_status")
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
