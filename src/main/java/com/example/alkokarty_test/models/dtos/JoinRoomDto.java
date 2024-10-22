package com.example.alkokarty_test.models.dtos;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.alkokarty_test.models.Room}
 */
@Value
public class JoinRoomDto implements Serializable {
    String code;
    String password;
    String playerToken;
}