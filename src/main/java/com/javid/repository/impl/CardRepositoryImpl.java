package com.javid.repository.impl;

import com.javid.database.DatabaseConnection;
import com.javid.model.Account;
import com.javid.model.Card;
import com.javid.repository.PrimitiveHandler;
import com.javid.repository.CardRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/16/2022
 */
public class CardRepositoryImpl implements CardRepository {

    private Connection connection;
    private static final String ID = "id";
    private static final String CVV2 = "cvv2";
    private static final String EXPIRE_DATE = "expire_date";
    private static final String ENABLED = "enabled";
    private static final String CARD_NUMBER = "card_number";
    private static final String PASSWORD1 = "password1";
    private static final String PASSWORD2 = "password2";
    private static final String ACCOUNT_ID = "account_id";
    private static final String SELECT_QUERY = """
            SELECT id, cvv2, expire_date, enabled, card_number, password1, password2, account_id
            FROM card
            WHERE 1=1
            %s;""";

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Card> findAll() {
        setConnection();
        List<Card> cards = new ArrayList<>();
        String query = SELECT_QUERY.formatted("ORDER BY id");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cards.add(parseCard(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cards;
    }

    @Override
    public Card findById(Long id) {
        setConnection();
        String query = SELECT_QUERY.formatted("""
                AND id = ?
                ORDER BY id""");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseCard(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Long save(Card entity) {
        setConnection();
        String query = """
                INSERT INTO card(cvv2, expire_date, enabled, card_number, password1, password2, account_id)
                values (?, ?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            PrimitiveHandler.setInt(statement, entity.getCvv2() == null
                    , 1, entity::getCvv2);
            statement.setDate(2, entity.getExpireDate());
            statement.setBoolean(3, entity.isEnabled());
            PrimitiveHandler.setLong(statement, entity.getNumber() == null
                    , 4, entity::getNumber);
            PrimitiveHandler.setInt(statement, entity.getPassword1()==null
            , 5, entity::getPassword1);
            PrimitiveHandler.setInt(statement, entity.getPassword2()==null
                    , 6, entity::getPassword2);
            PrimitiveHandler.setLong(statement, entity.getAccount() == null || entity.getAccount().isNew()
                    , 7, () -> entity.getAccount().getId());
            statement.execute();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(ID);
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteById(Long id) {
        setConnection();
        String query = """
                DELETE FROM card
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
    public void update(Card entity) {
        setConnection();
        String query = """
                UPDATE card
                SET cvv2 =?,
                    expire_date =?,
                    enabled =?,
                    card_number =?,
                    password1 =?,
                    password2 =?,
                    account_id =?
                WHERE id =?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            PrimitiveHandler.setInt(statement, entity.getCvv2() == null
                    , 1, entity::getCvv2);
            statement.setDate(2, entity.getExpireDate());
            statement.setBoolean(3, entity.isEnabled());
            PrimitiveHandler.setLong(statement, entity.getNumber() == null
                    , 4, entity::getNumber);
            PrimitiveHandler.setInt(statement, entity.getPassword1()==null
                    , 5, entity::getPassword1);
            PrimitiveHandler.setInt(statement, entity.getPassword2()==null
                    , 6, entity::getPassword2);
            PrimitiveHandler.setLong(statement, entity.getAccount() == null || entity.getAccount().isNew()
                    , 7, () -> entity.getAccount().getId());
            statement.setLong(8, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Date getCurrentDate() {
        String query = "SELECT (current_date)::DATE;";
        return getDate(query);
    }

    @Override
    public Date getExpireDate(int year) {
        String query = """
                SELECT (date_trunc('MONTH', current_date::date) + INTERVAL '%s YEAR + 1 MONTH - 1 day')::DATE;
                """.formatted(year);
        return getDate(query);
    }

    @Override
    public Date getExpireDate(Date date) {
        String query = """
                SELECT (date_trunc('MONTH', '%s'::date) + INTERVAL '1 MONTH - 1 day')::DATE;
                """.formatted(date.toString());
        return getDate(query);
    }

    private Date getDate(String query) {
        setConnection();
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getDate(1);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Card findByCardNumber(Long cardNumber) {
        setConnection();
        String query = SELECT_QUERY.formatted("""
                AND card_number = ?
                ORDER BY id""");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseCard(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Card parseCard(ResultSet resultSet) throws SQLException {
        long tempId = resultSet.getLong(ACCOUNT_ID);
        Account account = new Account()
                .setId(resultSet.wasNull() ? null : tempId);

        return new Card()
                .setId(resultSet.getLong(ID))
                .setCvv2(resultSet.getInt(CVV2))
                .setExpireDate(resultSet.getDate(EXPIRE_DATE))
                .setEnabled(resultSet.getBoolean(ENABLED))
                .setNumber(resultSet.getLong(CARD_NUMBER))
                .setPassword1(resultSet.getInt(PASSWORD1))
                .setPassword2(resultSet.getInt(PASSWORD2))
                .setAccount(account);
    }
}
