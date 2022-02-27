package com.javid.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
public class Customer extends BaseEntity {

    private String firstname;
    private String lastname;

    @Column(name = "national_code")
    private Long nationalCode;

    @OneToMany
    @JoinColumn(name = "customer_id")
    private Set<Account> accounts = new HashSet<>();

    @Override
    public Customer setId(Long id) {
        super.setId(id);
        return this;
    }

    @Override
    public String toString() {
        return "{ " +
               "id=" + getId() +
               ", firstname='" + firstname + '\'' +
               ", lastname='" + lastname + '\'' +
               ", nationalCode=" + nationalCode +
               " }";
    }
}
