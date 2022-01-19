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
public class Employee extends Person {

    private String username;
    private String password;
    private Employee manager;
    private Branch branch;

    @Override
    public String toString() {
        return "{ " +
                "id=" + getId() +
                ", firstname='" + getFirstname() + '\'' +
                ", lastname='" + getLastname() + '\'' +
                ", nationalCode='" + getNationalCode() + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", managerId=" + (manager == null ? "no manager" : manager.getId()) +
                ", branchId=" + (branch == null ? "no branch" : branch.getId()) +
                " }";
    }
}
