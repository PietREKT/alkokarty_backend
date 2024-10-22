package com.example.cards_game.data;

import com.example.cards_game.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByToken(String token);
}
