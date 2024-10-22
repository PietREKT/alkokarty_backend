package com.example.cards_game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class})
public class CardsGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsGameApplication.class, args);
    }

}
