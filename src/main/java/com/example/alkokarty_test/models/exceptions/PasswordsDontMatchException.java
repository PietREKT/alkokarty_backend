package com.example.alkokarty_test.models.exceptions;

public class PasswordsDontMatchException extends Exception{
    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
