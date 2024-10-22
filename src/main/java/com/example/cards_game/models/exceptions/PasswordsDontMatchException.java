package com.example.cards_game.models.exceptions;

public class PasswordsDontMatchException extends Exception{
    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
