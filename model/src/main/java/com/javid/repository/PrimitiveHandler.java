package com.javid.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author javid
 * Created on 1/23/2022
 */
@FunctionalInterface
public interface PrimitiveHandler<I> {

    static void setLong(PreparedStatement statement, boolean isNull, int index, PrimitiveHandler<Long> handler) throws SQLException {
        if (isNull) {
            setNull(statement, index);
        } else {
            statement.setLong(index, handler.get());
        }
    }

    static void setInt(PreparedStatement statement, boolean isNull, int index, PrimitiveHandler<Integer> handler) throws SQLException {
        if (isNull) {
            setNull(statement, index);
        } else {
            statement.setInt(index, handler.get());
        }
    }

    private static void setNull(PreparedStatement statement, int index) throws SQLException {
        statement.setNull(index, Types.BIGINT);
    }

    I get();
}
