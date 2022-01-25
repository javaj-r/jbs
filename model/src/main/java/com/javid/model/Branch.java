package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

/**
 * @author javid
 * Created on 1/15/2022
 */
@Getter
@Setter
@Accessors(chain = true)
public class Branch extends BaseEntity {

    private String name;
    private Employee manager;
    private Set<Employee> employees = new HashSet<>();

    @Override
    public Branch setId(Long id) {
        super.setId(id);
        return this;
    }

    @Override
    public String toString() {
        return "{ " +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", managerId=" + (manager == null ? "'no manager'" : manager.getId()) +
                " }";
    }
}
