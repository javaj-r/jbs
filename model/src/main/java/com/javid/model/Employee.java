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
public class Employee extends BaseEntity{

    private String username;
    private String password;
    private EmployeeRole role;
    private Employee manager;
    private Branch branch;

    @Override
    public Employee setId(Long id) {
        super.setId(id);
        return this;
    }

    @Override
    public String toString() {
        return "{ " +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", managerId=" + (manager == null ? "'no manager'" : manager.getId()) +
                ", branchId=" + (branch == null ? "'no branch'" : branch.getId()) +
                " }";
    }
}
