package com.example.cards_game.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String name;

    String token;

    boolean isInRoom = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
            @JsonIgnore
    Room room;


    boolean voted = false;

    public Player(String name) {
        this.name = name;
    }

    public String toString(){
        return "PLayer: {name: " + name + ", token: " + token + "}";
    }

    public boolean equals(Player p){
        return this.token.equals(p.token);
    }
}
