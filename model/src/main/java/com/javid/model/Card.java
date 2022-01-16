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

}
