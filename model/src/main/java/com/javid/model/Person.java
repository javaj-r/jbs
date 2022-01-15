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
public class Person extends User {

    private String firstname;
    private String lastname;
    private Long nationalCode;
    private String email;
    private Long phoneNumber;
    private Address address;

}
