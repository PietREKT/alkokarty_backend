package com.example.alkokarty_test.data;

import com.example.alkokarty_test.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByToken(String token);
}
