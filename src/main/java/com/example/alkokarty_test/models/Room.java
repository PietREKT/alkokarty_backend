package com.example.alkokarty_test.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    String code;
    int maxPlayers;
    String password;

    String hostToken;

    @Builder.Default
    boolean playing = false;

    String currentCard;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
            @Builder.Default
    List<Player> players = new ArrayList<>();

    @Builder.Default
    int playersVotedCount = 0;

    @Getter
    @JsonIgnore
    @Builder.Default
    final boolean keepAlive = false;

    public void zeroVotedCount(){
        playersVotedCount = 0;
    }

    public void incVotedCount(){
        playersVotedCount++;
    }

    public void decVotedCount(){ playersVotedCount--;}

    public boolean hasEmptySlots(){
        return players.size() < maxPlayers;
    }

}
