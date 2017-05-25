package com.prestu.gambler.exceptions;

public class UserIsOnlineException extends RuntimeException {
    public UserIsOnlineException() {
        super("Пользователь уже онлайн");
    }
}
