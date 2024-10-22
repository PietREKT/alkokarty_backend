package com.example.cards_game.models.exceptions;

public class PlayerLimitReachedException extends Exception{
    public PlayerLimitReachedException(String message) {
        super(message);
    }
}
