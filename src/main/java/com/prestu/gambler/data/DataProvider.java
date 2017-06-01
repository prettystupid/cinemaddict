package com.prestu.gambler.data;

import com.prestu.gambler.domain.ScoreInfo;
import com.prestu.gambler.domain.User;

import java.util.List;

public interface DataProvider {

    User authenticate(String userName, int password);
    void registerUser(String userName, int passwordHash, String firstName, String lastName, String email);
    void updateUser(User user);
    void saveScore(long userId, long gameId, int score);
    List<ScoreInfo> getScoreInfosForGame(long gameId, String gameName);
}
