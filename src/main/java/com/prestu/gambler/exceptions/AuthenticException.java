package com.prestu.gambler.exceptions;

public class AuthenticException extends RuntimeException {
    public AuthenticException() {
        super("Неверное имя пользователя или пароль");
    }
}
