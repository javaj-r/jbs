package com.javid.repository.jdbc;

import com.javid.database.DatabaseConnection;
import com.javid.model.*;
import com.javid.repository.TransactionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/17/2022
 */
public class TransactionRepositoryImpl implements TransactionRepository {

    private Connection connection;

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Transaction> findAll() {
        setConnection();
        List<Transaction> transactions = new ArrayList<>();
        String query = """
                SELECT id, amount, t_time, t_date, t_type, t_status, account_id
                FROM transactions;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong("account_id"));

                Transaction transaction = new Transaction()
                        .setAccount(account)
                        .setAmount(resultSet.getLong("amount"))
                        .setTime(resultSet.getTime("t_time"))
                        .setDate(resultSet.getDate("t_date"))
                        .setType(TransactionType.valueOf(resultSet.getString("t_type")))
                        .setStatus(TransactionStatus.valueOf(resultSet.getString("t_status")));
                transaction.setId(resultSet.getLong("id"));

                transactions.add(transaction);
            }
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public Transaction findById(Long id) {
        setConnection();
        Transaction transaction = new Transaction();
        String query = """
                SELECT id, amount, t_time, t_date, t_type, t_status, account_id
                FROM transactions
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong("account_id"));

                transaction.setAccount(account)
                        .setAmount(resultSet.getLong("amount"))
                        .setTime(resultSet.getTime("t_time"))
                        .setDate(resultSet.getDate("t_date"))
                        .setType(TransactionType.valueOf(resultSet.getString("t_type")))
                        .setStatus(TransactionStatus.valueOf(resultSet.getString("t_status")));
                transaction.setId(resultSet.getLong("id"));
            }
            return transaction;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transaction;
    }

    @Override
    public Long save(Transaction entity) {
        setConnection();
        String query = """
                INSERT INTO transactions(amount, account_id, t_time, t_date, t_type, t_status)
                VALUES (?, ?, current_time, current_date, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getAmount());
            statement.setLong(2, entity.getAccount().getId());
            statement.setString(3, entity.getType().name());
            statement.setString(4, entity.getStatus().name());
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
                DELETE FROM transactions
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
    public void update(Transaction entity) {
        setConnection();
        String query = """
                UPDATE transactions
                SET amount=?,
                    account_id=?,
                    t_type=?,
                    t_status=?
                WHERE id=?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, entity.getAmount());
            statement.setLong(2, entity.getAccount().getId());
            statement.setString(3, entity.getType().name());
            statement.setString(4, entity.getStatus().name());
            statement.setLong(5, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
