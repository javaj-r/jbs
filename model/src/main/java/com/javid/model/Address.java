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
public class Address extends BaseEntity {

    private String country;
    private String state;
    private String city;
    private String street;
    private Long postalCode;

}
