package com.javid.repository.jdbc;

import com.javid.database.DatabaseConnection;
import com.javid.model.Account;
import com.javid.model.Card;
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

    public void setConnection() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Card> findAll() {
        setConnection();
        List<Card> cards = new ArrayList<>();
        String query = """
                SELECT id
                     , cvv2
                     , expire_date
                     , enabled
                     , card_number
                     , password1
                     , password2
                     , account_id
                FROM card;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong("account_id"));

                Card card = new Card()
                        .setCvv2(resultSet.getInt("cvv2"))
                        .setExpireDate(resultSet.getDate("expire_date"))
                        .setEnabled(resultSet.getBoolean("enabled"))
                        .setNumber(resultSet.getLong("card_number"))
                        .setPassword1(resultSet.getInt("password1"))
                        .setPassword2(resultSet.getInt("password2"));
                card.setId(resultSet.getLong("id"));

                cards.add(card);
            }

            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    @Override
    public Card findById(Long id) {
        setConnection();
        Card card = new Card();
        String query = """
                SELECT id
                     , cvv2
                     , expire_date
                     , enabled
                     , card_number
                     , password1
                     , password2
                     , account_id
                FROM card
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong("account_id"));

                card.setCvv2(resultSet.getInt("cvv2"))
                        .setExpireDate(resultSet.getDate("expire_date"))
                        .setEnabled(resultSet.getBoolean("enabled"))
                        .setNumber(resultSet.getLong("card_number"))
                        .setPassword1(resultSet.getInt("password1"))
                        .setPassword2(resultSet.getInt("password2"));
                card.setId(resultSet.getLong("id"));
            }

            return card;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    @Override
    public Long save(Card entity) {
        setConnection();
        String query = """
                INSERT INTO card(cvv2, expire_date, enabled, account_id)
                values (?, ?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.getCvv2());
            statement.setDate(2, entity.getExpireDate());
            statement.setBoolean(3, entity.isEnabled());
            if (entity.getAccount() == null || entity.getAccount().isNew()) {
                statement.setNull(4, Types.BIGINT);
            } else {
                statement.setLong(4, entity.getAccount().getId());
            }
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
                SET cvv2=?,
                    expire_date=?,
                    enabled =?,
                    card_number=?,
                    password1 =?,
                    password2 =?,
                    account_id =?
                WHERE id=?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, entity.getCvv2());
            statement.setDate(2, entity.getExpireDate());
            statement.setBoolean(3, entity.isEnabled());
            statement.setLong(4, entity.getNumber());
            statement.setInt(5, entity.getPassword1());
            statement.setInt(6, entity.getPassword2());

            if (entity.getAccount() == null || entity.getAccount().isNew()) {
                statement.setNull(7, Types.BIGINT);
            } else {
                statement.setLong(7, entity.getAccount().getId());
            }
            statement.setLong(8, entity.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
