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
public class BaseEntity {

    private Long id;

    public boolean isNew() {
        return id == null;
    }
}
