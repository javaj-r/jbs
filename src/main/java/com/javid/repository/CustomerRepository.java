package com.javid.repository;

import com.javid.model.Customer;

/**
 * @author javid
 * Created on 1/16/2022
 */
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Customer findByNationalCode(Customer entity);
}
