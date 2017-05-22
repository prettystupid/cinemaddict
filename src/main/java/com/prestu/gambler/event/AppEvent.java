package com.prestu.gambler.event;

import com.prestu.gambler.view.ViewType;

public abstract class AppEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;

        public UserLoginRequestedEvent(final String userName,
                final String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
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
