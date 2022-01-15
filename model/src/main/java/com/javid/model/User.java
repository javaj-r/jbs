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
public class User extends BaseEntity {

    private String username;
    private String password;
    private Boolean enabled;
    private Set<Role> roles;

}
