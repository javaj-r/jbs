package com.javid.repository;

import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public interface CrudRepository<T, I> {

    List<T> findAll();

    T findById(I id);

    I save(T entity);

    void deleteById(I id);

    void update(T entity);

}
