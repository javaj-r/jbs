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
public class Branch extends NamedEntity{

    private Bank bank;
    private Employee manager;
    private Set<Employee> employees;

    @Override
    public String toString() {
        return "{ " +
                "id=" + getId() +
                ", name=" + getName() +
                ", bankId=" + (bank == null ? "no manager" : bank.getId()) +
                ", managerId=" + (manager == null ? "no manager" : manager.getId()) +
                " }";
    }
}
