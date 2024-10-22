package com.example.alkokarty_test.data;

import com.example.alkokarty_test.models.Player;
import com.example.alkokarty_test.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    Optional<Room> findByCode(String code);

    Optional<Room> findByPlayers_Token(String token);

}
