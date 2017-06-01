package com.prestu.gambler.data;

import com.prestu.gambler.domain.ScoreInfo;
import com.prestu.gambler.domain.User;
import com.prestu.gambler.exceptions.AuthenticException;
import com.prestu.gambler.exceptions.UserExistsException;
import com.prestu.gambler.utils.Notifications;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GamblerDataProvider implements DataProvider {

    private Connection getConnection() throws SQLException {
        XMLConfiguration config;
        try {
            config = new XMLConfiguration();
            config.load(GamblerDataProvider.class.getResourceAsStream("config.xml"));
        } catch (ConfigurationException e) {
            throw new RuntimeException("Config is not found");
        }

        String url = "jdbc:mysql://localhost:3306/" + config.getString("db-name") + "?useSSL=false&serverTimezone=UTC";
        return DriverManager.getConnection(url, config.getString("username"), config.getString("password"));
    }

    private PreparedStatement createPreparedStatement(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    private ResultSet getResultSet(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeQuery();
    }

    private void close(ResultSet resultSet, PreparedStatement statement, Connection connection) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public User authenticate(String userName, int password) {
        return getUserByName(userName, password);
    }

    @Override
    public void registerUser(String userName, int passwordHash, String firstName, String lastName, String email) {
        try {
            Connection connection = getConnection();
            PreparedStatement statement = createPreparedStatement(connection, "INSERT INTO `users` (`username`, `password`, `firstname`, `lastname`, `email`) VALUES (?, ?, ?, ?, ?);");
            statement.setString(1, userName);
            statement.setInt(2, passwordHash);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, email);
            statement.execute();
            close(null, statement, connection);
            Notifications.show("Добро пожаловать на GAMBLER", "Регистрация прошла успешно", Notifications.SMALL_WINDOW);
        } catch (SQLIntegrityConstraintViolationException duplicateEx) {
            throw new UserExistsException();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Notifications.show("Ошибка", "Повторите попытку", Notifications.SMALL_WINDOW);
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            Connection connection = getConnection();
            String query = "UPDATE `users` SET firstname = ?, lastname = ?, email = ?, male = ?, city = ?, site = ?, bio = ?, logo_path = ? WHERE username = ?";
            PreparedStatement statement = createPreparedStatement(connection, query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setBoolean(4, user.isMale());
            statement.setString(5, user.getCity());
            statement.setString(6, user.getWebsite());
            statement.setString(7, user.getBio());
            statement.setString(8, user.getLogo().getResourceId());
            statement.setString(9, user.getUsername());
            statement.execute();
            close(null, statement, connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveScore(long userId, long gameId, int score) {
        int lastScore = getScore(userId, gameId);
        if (lastScore > score) return;
        try {
            Connection connection = getConnection();
            PreparedStatement statement = createPreparedStatement(connection,
                    "INSERT INTO `scores` (`game_id`, `user_id`, `score`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE score = ?;");
            statement.setLong(1, gameId);
            statement.setLong(2, userId);
            statement.setInt(3, score);
            statement.setInt(4, score);
            statement.execute();
            close(null, statement, connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<ScoreInfo> getScoreInfosForGame(long gameId, String gameName) {
        List<ScoreInfo> scoreInfos = new ArrayList<>();
        try {
            Connection connection = getConnection();
            String query = "SELECT username, score FROM `scores`, `users` WHERE users.id = user_id AND game_id = ?";
            PreparedStatement statement = createPreparedStatement(connection, query);
            statement.setLong(1, gameId);
            ResultSet resultSet = getResultSet(statement);
            while (resultSet.next()) {
                ScoreInfo scoreInfo = new ScoreInfo(resultSet.getString("username"), gameName, resultSet.getInt("score"));
                scoreInfos.add(scoreInfo);
            }
            close(resultSet, statement, connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return scoreInfos;
    }

    private int getScore(long userId, long gameId) {
        int score = 0;
        try {
            Connection connection = getConnection();
            String query = "SELECT * FROM `scores` WHERE user_id = ? AND game_id = ?";
            PreparedStatement statement = createPreparedStatement(connection, query);
            statement.setLong(1, userId);
            statement.setLong(2, gameId);
            ResultSet resultSet = getResultSet(statement);
            if (resultSet.next()) {
                score = resultSet.getInt("score");
            }
            close(resultSet, statement, connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return score;
    }

    private User getUserByName(String username, int password) throws AuthenticException {
        User user = new User();
        try {
            Connection connection = getConnection();
            String query = "SELECT * FROM `users` WHERE username = ?";
            PreparedStatement statement = createPreparedStatement(connection, query);
            statement.setString(1, username);
            ResultSet resultSet = getResultSet(statement);
            if (!resultSet.next() || resultSet.getInt("password") != password) {
                throw new AuthenticException();
            } else {
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setFirstName(resultSet.getString("firstname"));
                user.setLastName(resultSet.getString("lastname"));
                user.setMale(resultSet.getBoolean("male"));
                user.setCity(resultSet.getString("city"));
                user.setEmail(resultSet.getString("email"));
                user.setWebsite(resultSet.getString("site"));
                user.setBio(resultSet.getString("bio"));
                user.setLogo(new ThemeResource(resultSet.getString("logo_path")));
            }
            close(resultSet, statement, connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    private User getCurrentUser() {
        return (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());
    }
}
