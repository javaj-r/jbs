package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author javid
 * Created on 1/15/2022
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class Employee extends BaseEntity {

    private String username;
    private String password;
    private EmployeeRole role;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "branch_id")
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
               ", Role=" + (role == null ? null : role.name()) +
               ", managerId=" + (manager == null ? "'no manager'" : manager.getId()) +
               ", branchId=" + (branch == null ? "'no branch'" : branch.getId()) +
               " }";
    }
}
