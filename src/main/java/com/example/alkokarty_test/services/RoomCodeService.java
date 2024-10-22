package com.example.alkokarty_test.services;

import com.example.alkokarty_test.models.dtos.CreateRoomDto;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Service
public class RoomCodeService {
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String generateRoomCode(CreateRoomDto roomDto) throws NoSuchAlgorithmException {
        String base = roomDto.getHostToken() + roomDto.getMaxPlayers() + new Date();

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] hashBytes = digest.digest(base.getBytes(StandardCharsets.UTF_8));

        return convertToAlphanumeric(hashBytes, 5);
    }

    private String convertToAlphanumeric(byte[] hashBytes, int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();

        for(byte b : hashBytes){
            int index = (b & 0xFF) % ALPHANUMERIC.length();
            sb.append(ALPHANUMERIC.charAt(index));

            if (sb.length() == length)
                break;
        }

        while(sb.length() < length){
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }

        return sb.toString();
    }
}
