package com.prestu.gambler.event;

import com.prestu.gambler.domain.User;
import com.prestu.gambler.games.Game;
import com.prestu.gambler.view.ViewType;

public abstract class AppEvent {

    public static class UserLoginRequestedEvent {
        private String userName;
        private int passwordHash;

        public UserLoginRequestedEvent(String userName, int passwordHash) {
            this.userName = userName;
            this.passwordHash = passwordHash;
        }

        public String getUserName() {
            return userName;
        }

        public int getPasswordHash() {
            return passwordHash;
        }
    }

    public static class UserRegisteredEvent {
        private String userName;
        private int passwordHash;
        private String firstName;
        private String lastName;
        private String email;

        public UserRegisteredEvent(String userName, int passwordHash, String firstName, String lastName, String email) {
            this.userName = userName;
            this.passwordHash = passwordHash;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        public String getUserName() {
            return userName;
        }

        public int getPasswordHash() {
            return passwordHash;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }
    }

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {
        private String username;

        public UserLoggedOutEvent(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class PostViewChangeEvent {
        private ViewType view;

        public PostViewChangeEvent(ViewType view) {
            this.view = view;
        }

        public ViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
    }

    public static class EndGameEvent {

        private Long gameId;
        private int score;

        public EndGameEvent(Long gameId, int score) {
            this.gameId = gameId;
            this.score = score;
        }

        public int getScore() {
            return score;
        }

        public Long getGameId() {
            return gameId;
        }
    }

    public static class ProfileUpdatedEvent {

        private User user;

        public ProfileUpdatedEvent(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

}
