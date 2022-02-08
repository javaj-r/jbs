package com.javid.repository.impl;

import com.javid.database.DatabaseConnection;
import com.javid.model.Account;
import com.javid.model.Transaction;
import com.javid.model.TransactionStatus;
import com.javid.model.TransactionType;
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
    private static final String ID = "id";
    private static final String AMOUNT = "amount";
    private static final String ACCOUNT_ID = "account_id";
    private static final String TIME = "t_time";
    private static final String DATE = "t_date";
    private static final String TYPE = "t_type";
    private static final String STATUS = "t_status";
    private static final String SELECT_QUERY = """
            SELECT id, amount, t_time, t_date, t_type, t_status, account_id
            FROM transactions
            WHERE 1=1
            %s;""";
    private static final String TRANSACTION_QUERY = """
            UPDATE account SET balance=balance + ?
            WHERE id = ?;
            INSERT INTO transactions(amount, account_id, t_time, t_date, t_type, t_status)
            VALUES (?, ?, current_time, current_date, ?::transaction_type, ?::transaction_status);
            """;

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Transaction> findAll() {
        setConnection();
        List<Transaction> transactions = new ArrayList<>();
        String query = SELECT_QUERY.formatted("ORDER BY id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(parseTransaction(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public Transaction findById(Long id) {
        setConnection();
        String query = SELECT_QUERY.formatted("""
                AND id = ?
                ORDER BY id""");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseTransaction(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Long save(Transaction entity) {
        setConnection();
        String query = """
                INSERT INTO transactions(amount, account_id, t_time, t_date, t_type, t_status)
                VALUES (?, ?, current_time, current_date, ?::transaction_type, ?::transaction_status);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getAmount());
            statement.setLong(2, entity.getAccount().getId());
            statement.setString(3, entity.getType().name());
            statement.setString(4, entity.getStatus().name());
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
                    t_type=?::transaction_type,
                    t_status=?::transaction_status
                WHERE id = ?;
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

    @Override
    public List<Transaction> findAllByAccountIdAndStartDate(Account account, Date startDate) {
        setConnection();
        List<Transaction> transactions = new ArrayList<>();
        String query = SELECT_QUERY.formatted("""
                AND account_id = ?
                AND t_date >= ?
                ORDER BY id""");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, account.getId());
            statement.setDate(2, startDate);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(parseTransaction(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    @Override
    public List<Long> transfer(Transaction source, Transaction destination) {
        setConnection();
        List<Long> idList = new ArrayList<>();
        String query = TRANSACTION_QUERY.repeat(2);
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, source.getAmount());
            statement.setLong(2, source.getAccount().getId());

            statement.setLong(3, source.getAmount());
            statement.setLong(4, source.getAccount().getId());
            statement.setString(5, source.getType().name());
            statement.setString(6, source.getStatus().name());

            statement.setLong(7, destination.getAmount());
            statement.setLong(8, destination.getAccount().getId());

            statement.setLong(9, destination.getAmount());
            statement.setLong(10, destination.getAccount().getId());
            statement.setString(11, destination.getType().name());
            statement.setString(12, destination.getStatus().name());

            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            while (resultSet.next()) {
                idList.add(resultSet.getLong(ID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idList;
    }


    private Transaction parseTransaction(ResultSet resultSet) throws SQLException {
        long tempId = resultSet.getLong(ACCOUNT_ID);
        Account account = new Account()
                .setId(resultSet.wasNull() ? null : tempId);

        String transactionType = resultSet.getString(TYPE);
        String transactionStatus = resultSet.getString(STATUS);

        return new Transaction()
                .setId(resultSet.getLong(ID))
                .setAmount(resultSet.getLong(AMOUNT))
                .setAccount(account)
                .setTime(resultSet.getTime(TIME))
                .setDate(resultSet.getDate(DATE))
                .setType(transactionType == null ? null : TransactionType.valueOf(transactionType))
                .setStatus(transactionStatus == null ? null : TransactionStatus.valueOf(transactionStatus));
    }
}
