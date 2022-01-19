package com.javid.repository.jdbc;

import com.javid.database.DatabaseConnection;
import com.javid.model.Account;
import com.javid.model.Branch;
import com.javid.model.Card;
import com.javid.model.Customer;
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

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Account> findAll() {
        setConnection();
        List<Account> accounts = new ArrayList<>();
        String query = """
                SELECT id
                     , balance
                     , enabled
                     , branch_id
                     , card_id
                     , customer_id
                FROM account;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Branch branch = new Branch();
                branch.setId(resultSet.getLong("branch_id"));

                Card card = new Card();
                card.setId(resultSet.getLong("card_id"));

                Customer customer = new Customer();
                customer.setId(resultSet.getLong("customer_id"));

                Account account = new Account()
                        .setBalance(resultSet.getLong("balance"))
                        .setEnabled(resultSet.getBoolean("enabled"))
                        .setBranch(branch)
                        .setCard(card)
                        .setCustomer(customer);
                account.setId(resultSet.getLong("id"));

                accounts.add(account);
            }

            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    @Override
    public Account findById(Long id) {
        setConnection();
        Account account = new Account();
        String query = """
                SELECT id
                     , balance
                     , enabled
                     , branch_id
                     , card_id
                     , customer_id
                FROM account
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Branch branch = new Branch();
                branch.setId(resultSet.getLong("branch_id"));

                Card card = new Card();
                card.setId(resultSet.getLong("card_id"));

                Customer customer = new Customer();
                customer.setId(resultSet.getLong("customer_id"));

                account.setBalance(resultSet.getLong("balance"))
                        .setEnabled(resultSet.getBoolean("enabled"))
                        .setBranch(branch)
                        .setCard(card)
                        .setCustomer(customer);
                account.setId(resultSet.getLong("id"));
            }

            return account;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return account;
    }

    @Override
    public Long save(Account entity) {
        setConnection();
        String query = """
                INSERT INTO account(enabled, balance, customer_id, branch_id)
                values (?, ?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBoolean(1, entity.isEnabled());
            statement.setLong(2, entity.getBalance());
            statement.setLong(3, entity.getCustomer().getId());
            statement.setLong(4, entity.getBranch().getId());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong("id");
            }
            return null;
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
                    card_id=?
                WHERE id=?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, entity.isEnabled());
            statement.setLong(2, entity.getBalance());
            if (entity.getCard() == null || entity.getCard().isNew()) {
                statement.setNull(3, Types.BIGINT);
            } else {
                statement.setLong(3, entity.getCard().getId());
            }
            statement.setLong(4, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
