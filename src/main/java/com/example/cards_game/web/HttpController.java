package com.example.cards_game.web;

import com.example.cards_game.data.RoomRepository;
import com.example.cards_game.data.UserRepository;
import com.example.cards_game.models.Player;
import com.example.cards_game.models.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Slf4j
@CrossOrigin("*")
public class HttpController {

    private final UserRepository userRepo;

    private final RoomRepository roomRepo;

    public HttpController(UserRepository userRepo, RoomRepository roomRepo) {
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
    }

    @GetMapping("/player/validate")
    public ResponseEntity<Boolean> validate(@RequestParam String token){
        log.info("Validating token: {}", token);
        Optional<Player> player = userRepo.findByToken(token);
        return ResponseEntity.ok(player.isPresent());
    }

    @PostMapping("/player/activate")
    public ResponseEntity<?> setPlayerActive(@RequestBody String token){
        log.info("Making player active. token = {}", token);
        Player player = userRepo.findByToken(token).orElseThrow();
        player.setInRoom(true);
        userRepo.save(player);
        return ResponseEntity.ok("{Token: \"" + token + "\"}");
    }

    @GetMapping("/room/getRoom")
    public ResponseEntity<Room> getRoom(@RequestParam String playerToken){
        log.info("Trying to retrieve room by playerToken: {}", playerToken);
        Optional<Room> room = roomRepo.findByPlayers_Token(playerToken);
        if (room.isEmpty())
            return ResponseEntity.status(HttpStatusCode.valueOf(404)).body(null);
        return ResponseEntity.ok(room.get());
    }
}
