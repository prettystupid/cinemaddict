package com.prestu.gambler.data;

import com.prestu.gambler.domain.Movie;
import com.prestu.gambler.domain.MovieRevenue;
import com.prestu.gambler.domain.Transaction;
import com.prestu.gambler.domain.User;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import java.sql.*;
import java.util.Collection;
import java.util.Date;

public class GamblerDataProvider implements DataProvider {

    protected Connection getConnection() throws SQLException {
        XMLConfiguration config;
        try {
            config = new XMLConfiguration("src\\main\\resources\\config.xml");
        } catch (ConfigurationException e) {
            throw new RuntimeException("Config is not found");
        }

        String url = new StringBuilder("jdbc:mysql://localhost:3306/").append(config.getString("db-name")).append("?useSSL=false&serverTimezone=UTC").toString();
        return DriverManager.getConnection(url, config.getString("username"), config.getString("password"));
    }

    protected PreparedStatement createPreparedStatement(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    protected ResultSet getResultSet(PreparedStatement preparedStatement) throws SQLException {
        return preparedStatement.executeQuery();
    }

    protected void close(ResultSet resultSet, PreparedStatement statement, Connection connection) throws SQLException {
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
    public Collection<Transaction> getRecentTransactions(int count) {
        return null;
    }

    @Override
    public Collection<MovieRevenue> getDailyRevenuesByMovie(long id) {
        return null;
    }

    @Override
    public Collection<MovieRevenue> getTotalMovieRevenues() {
        return null;
    }

    @Override
    public User authenticate(String userName, int password) {
        return null;
    }

    @Override
    public void registerUser(String userName, int passwordHash, String firstName, String lastName, String email) {

    }

    @Override
    public void updateUser(User user) {

    }

    @Override
    public void logOutUser(String username) {

    }

    @Override
    public Collection<Movie> getMovies() {
        return null;
    }

    @Override
    public Movie getMovie(long movieId) {
        return null;
    }

    @Override
    public Collection<Transaction> getTransactionsBetween(Date startDate, Date endDate) {
        return null;
    }
}
