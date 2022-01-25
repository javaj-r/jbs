package com.javid.model;

import java.util.Arrays;

/**
 * @author javid
 * Created on 1/21/2022
 */
public enum EmployeeRole {
    ADMIN, BANK_MANAGER, BRANCH_MANAGER, CASHIER;

    public static String[] stringValues() {
        return Arrays.stream(EmployeeRole.values())
                .map(EmployeeRole::name)
                .toArray(String[]::new);
    }
}
