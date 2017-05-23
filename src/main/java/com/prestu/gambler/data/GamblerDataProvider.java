package com.prestu.gambler.data;

import com.prestu.gambler.domain.Movie;
import com.prestu.gambler.domain.MovieRevenue;
import com.prestu.gambler.domain.Transaction;
import com.prestu.gambler.domain.User;

import java.util.Collection;
import java.util.Date;

public class GamblerDataProvider implements DataProvider {
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
