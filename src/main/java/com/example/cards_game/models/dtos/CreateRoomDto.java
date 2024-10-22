package com.example.cards_game.models.dtos;

import jakarta.annotation.Nullable;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.cards_game.models.Room}
 */
@Value
public class CreateRoomDto implements Serializable {
    int maxPlayers;
    @Nullable
    String password;
    String hostToken;
}