package com.prestu.cinemaddict.data;

import com.prestu.cinemaddict.domain.Movie;
import com.prestu.cinemaddict.domain.MovieRevenue;
import com.prestu.cinemaddict.domain.Transaction;
import com.prestu.cinemaddict.domain.User;

import java.util.Collection;
import java.util.Date;

public interface DataProvider {

    Collection<Transaction> getRecentTransactions(int count);
    Collection<MovieRevenue> getDailyRevenuesByMovie(long id);
    Collection<MovieRevenue> getTotalMovieRevenues();
    User authenticate(String userName, String password);
    Collection<Movie> getMovies();
    Movie getMovie(long movieId);
    Collection<Transaction> getTransactionsBetween(Date startDate, Date endDate);
}
