package com.example.cards_game.models.dtos;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.cards_game.models.Room}
 */
@Value
public class JoinRoomDto implements Serializable {
    String code;
    String password;
    String playerToken;
}