package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Date;

/**
 * @author javid
 * Created on 1/15/2022
 */
@Getter
@Setter
@Accessors(chain = true)
public class Card extends BaseEntity {

    private Account account;
    private Long number;
    private Date expireDate;
    private Integer cvv2;
    private Integer password1;
    private Integer password2;
    private boolean enabled;

    @Override
    public Card setId(Long id) {
        super.setId(id);
        return this;
    }

    @Override
    public String toString() {
        return "{ " +
                "id=" + getId() +
                ", accountId=" + (account == null ? "'no account'" : account.getId()) +
                ", number=" + number +
                ", expireDate=" + expireDate +
                ", cvv2=" + cvv2 +
                ", password1=" + password1 +
                ", password2=" + password2 +
                ", enabled=" + enabled +
                " }";
    }
}
