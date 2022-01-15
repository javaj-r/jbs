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
public class NamedEntity extends BaseEntity{

    private String name;
}
