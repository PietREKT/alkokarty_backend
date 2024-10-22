package com.example.cards_game.data;

import com.example.cards_game.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    Optional<Room> findByCode(String code);

    Optional<Room> findByPlayers_Token(String token);

}
