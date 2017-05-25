package com.prestu.gambler.event;

import com.prestu.gambler.domain.User;
import com.prestu.gambler.view.ViewType;

public abstract class AppEvent {

    public static final class UserLoginRequestedEvent {
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

    public static final class UserRegisteredEvent {
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

    public static final class PostViewChangeEvent {
        private final ViewType view;

        public PostViewChangeEvent(final ViewType view) {
            this.view = view;
        }

        public ViewType getView() {
            return view;
        }
    }

    public static class CloseOpenWindowsEvent {
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
