package com.prestu.gambler.data;

import com.prestu.gambler.domain.Movie;
import com.prestu.gambler.domain.MovieRevenue;
import com.prestu.gambler.domain.Transaction;
import com.prestu.gambler.domain.User;

import java.util.Collection;
import java.util.Date;

public interface DataProvider {

    Collection<Transaction> getRecentTransactions(int count);
    Collection<MovieRevenue> getDailyRevenuesByMovie(long id);
    Collection<MovieRevenue> getTotalMovieRevenues();
    User authenticate(String userName, int password);
    Collection<Movie> getMovies();
    Movie getMovie(long movieId);
    Collection<Transaction> getTransactionsBetween(Date startDate, Date endDate);
}
