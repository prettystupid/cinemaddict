package com.prestu.gambler.exceptions;

public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super("Данное имя пользователя занято");
    }
}
