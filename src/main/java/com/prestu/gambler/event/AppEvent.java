package com.prestu.gambler.event;

import com.prestu.gambler.view.ViewType;

public abstract class AppEvent {

    public static final class UserLoginRequestedEvent {
        private String userName;
        private int passwordHash;

        public UserLoginRequestedEvent(final String userName,
                final int passwordHash) {
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

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

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
    }

}
