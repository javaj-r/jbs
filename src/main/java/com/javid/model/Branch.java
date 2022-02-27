package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

/**
 * @author javid
 * Created on 1/15/2022
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class Branch extends BaseEntity {

    private String name;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @OneToMany
    @JoinColumn(name = "branch_id")
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
