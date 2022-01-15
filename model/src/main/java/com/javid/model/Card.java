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
public class Card extends BaseEntity{

    private Account account;
    private Long number;
    private Integer password1;
    private Integer password2;
    private boolean enabled;


}
