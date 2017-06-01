package com.prestu.gambler;

import com.prestu.gambler.domain.User;
import com.prestu.gambler.exceptions.UserIsOnlineException;

import java.util.ArrayList;
import java.util.List;

public class OnlineUsersObserver {

    private static volatile OnlineUsersObserver instance;
    private static List<User> onlineUsers;

    private OnlineUsersObserver() {
        onlineUsers = new ArrayList<>();
    }

    public static OnlineUsersObserver getInstance() {
        OnlineUsersObserver localInstance = instance;
        if (localInstance == null) {
            synchronized (OnlineUsersObserver.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new OnlineUsersObserver();
                }
            }
        }
        return localInstance;
    }

    public synchronized void userSignIn(User user) {
        if (onlineUsers.contains(user)) {
            throw new UserIsOnlineException();
        } else {
            onlineUsers.add(user);
        }
    }

    public synchronized void userSignOut(User user) {
        onlineUsers.remove(user);
    }
}