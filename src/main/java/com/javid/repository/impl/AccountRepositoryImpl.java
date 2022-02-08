package com.javid.repository.impl;

import com.javid.database.DatabaseConnection;
import com.javid.model.Account;
import com.javid.model.Branch;
import com.javid.model.Card;
import com.javid.model.Customer;
import com.javid.repository.PrimitiveHandler;
import com.javid.repository.AccountRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class AccountRepositoryImpl implements AccountRepository {

    private Connection connection;
    private static final String ID = "id";
    private static final String BALANCE = "balance";
    private static final String ENABLED = "enabled";
    private static final String BRANCH_ID = "branch_id";
    private static final String CARD_ID = "card_id";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String SELECT_QUERY = """
            SELECT id, enabled, balance, customer_id, branch_id, card_id
            FROM account
            WHERE 1=1
            %s;""";


    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Account> findAll() {
        setConnection();
        List<Account> accounts = new ArrayList<>();
        String query = SELECT_QUERY.formatted("ORDER BY id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(parseAccount(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account findById(Long id) {
        setConnection();
        String query = SELECT_QUERY.formatted("""
                AND id = ?
                ORDER BY id""");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseAccount(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Long save(Account entity) {
        setConnection();
        String query = """
                INSERT INTO account(enabled, balance, customer_id, branch_id, card_id)
                values (?, ?, ?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBoolean(1, entity.isEnabled());

            boolean isNull = entity.getBalance() == null;
            PrimitiveHandler.setLong(statement, isNull, 2, entity::getBalance);

            isNull = entity.getCustomer() == null || entity.getCustomer().isNew();
            PrimitiveHandler.setLong(statement, isNull, 3, () -> entity.getCustomer().getId());

            isNull = entity.getBranch() == null || entity.getBranch().isNew();
            PrimitiveHandler.setLong(statement, isNull, 4, () -> entity.getBranch().getId());

            isNull = entity.getCard() == null || entity.getCard().isNew();
            PrimitiveHandler.setLong(statement, isNull, 5, () -> entity.getCard().getId());

            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(ID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteById(Long id) {
        setConnection();
        String query = """
                DELETE FROM account
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Account entity) {
        setConnection();
        String query = """
                UPDATE account
                SET enabled=?,
                    balance=?,
                    customer_id=?,
                    branch_id=?,
                    card_id=?
                WHERE id=?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, entity.isEnabled());

            boolean isNull = entity.getBalance() == null;
            PrimitiveHandler.setLong(statement, isNull, 2, entity::getBalance);

            isNull = entity.getCustomer() == null || entity.getCustomer().isNew();
            PrimitiveHandler.setLong(statement, isNull, 3, () -> entity.getCustomer().getId());

            isNull = entity.getBranch() == null || entity.getBranch().isNew();
            PrimitiveHandler.setLong(statement, isNull, 4, () -> entity.getBranch().getId());

            isNull = entity.getCard() == null || entity.getCard().isNew();
            PrimitiveHandler.setLong(statement, isNull, 5, () -> entity.getCard().getId());
            statement.setLong(6, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Account> findAllByCustomerId(Long customerId) {
        setConnection();
        List<Account> accounts = new ArrayList<>();
        String query = SELECT_QUERY.formatted("""
                AND customer_id = ?
                ORDER BY id""");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(parseAccount(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    private Account parseAccount(ResultSet resultSet) throws SQLException {
        long tempId = resultSet.getLong(BRANCH_ID);
        Branch branch = new Branch()
                .setId(resultSet.wasNull() ? null : tempId);

        tempId = resultSet.getLong(CARD_ID);
        Card card = new Card()
                .setId(resultSet.wasNull() ? null : tempId);

        tempId = resultSet.getLong(CUSTOMER_ID);
        Customer customer = new Customer()
                .setId(resultSet.wasNull() ? null : tempId);

        return new Account()
                .setId(resultSet.getLong(ID))
                .setBalance(resultSet.getLong(BALANCE))
                .setEnabled(resultSet.getBoolean(ENABLED))
                .setBranch(branch)
                .setCustomer(customer)
                .setCard(card);
    }
}
