package com.example.alkokarty_test.models.exceptions;

public class PlayerLimitReachedException extends Exception{
    public PlayerLimitReachedException(String message) {
        super(message);
    }
}
