package com.example.cards_game.web;

import com.example.cards_game.data.CardRepository;
import com.example.cards_game.data.RoomRepository;
import com.example.cards_game.data.UserRepository;
import com.example.cards_game.models.Card;
import com.example.cards_game.models.Player;
import com.example.cards_game.models.Room;
import com.example.cards_game.models.dtos.CreateRoomDto;
import com.example.cards_game.models.dtos.JoinRoomDto;
import com.example.cards_game.models.exceptions.MaxNumberOfVotesException;
import com.example.cards_game.models.exceptions.PasswordsDontMatchException;
import com.example.cards_game.models.exceptions.PlayerLimitReachedException;
import com.example.cards_game.models.exceptions.PlayerNotCreatedException;
import com.example.cards_game.services.RoomCodeService;
import com.example.cards_game.services.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Controller
@Slf4j
public class WebSocketController {
    private final UserRepository userRepo;

    private final RoomRepository roomRepo;

    private final CardRepository cardRepo;

    private final RoomService roomService;

    private SimpMessagingTemplate template;

    private final RoomCodeService roomCodeService;
    private final PasswordEncoder passwordEncoder;

    public WebSocketController(UserRepository userRepo, RoomRepository roomRepo, CardRepository cardRepo, RoomService roomService, RoomCodeService roomCodeService, SimpMessagingTemplate template, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
        this.cardRepo = cardRepo;
        this.roomService = roomService;
        this.roomCodeService = roomCodeService;
        this.template = template;
        this.passwordEncoder = passwordEncoder;
    }


    @MessageMapping("/users/addUser")
    @SendToUser("/queue/reply")
    public String addUser(@Payload String username, StompHeaderAccessor accessor) {
        Player player = userRepo.save(new Player(username));
        log.info("Received addUser: {}", player);
        String token = generateToken(player);
        accessor.getSessionAttributes().put("playerToken", token);
        return "Token: " + token;
    }

    @MessageMapping("/rooms/create")
    @SendToUser("/queue/reply")
    public Room createRoom(@Payload CreateRoomDto roomDto) throws NoSuchAlgorithmException {
        Player player = userRepo.findByToken(roomDto.getHostToken()).orElseThrow();
        Room room = Room.builder()
                .hostToken(roomDto.getHostToken())
                .maxPlayers(roomDto.getMaxPlayers())
                .password(roomDto.getPassword().isEmpty() ? null : passwordEncoder.encode(roomDto.getPassword()))
                .code(roomCodeService.generateRoomCode(roomDto))
                .playersVotedCount(0)
                .build();
        room = roomService.addPlayer(room, player);
        log.info("Created room: {}", room);
        return room;
    }

    @MessageMapping("/rooms/join/{roomCode}")
    @SendToUser("/queue/reply/join")
    @SendTo("/room/{roomCode}/join")
    public Room joinRoom(@DestinationVariable String roomCode, @Payload JoinRoomDto roomDto) throws PlayerLimitReachedException, PasswordsDontMatchException, PlayerNotCreatedException {
        Player player = userRepo.findByToken(roomDto.getPlayerToken()).orElseThrow(() ->
                new PlayerNotCreatedException("Nie udało się znaleźć gracza!"));
        Room room = roomRepo.findByCode(roomCode).orElseThrow();
        if (room.getHostToken() == null && room.getPlayers().isEmpty())
            room.setHostToken(player.getToken());

        if (!room.hasEmptySlots()) {
            throw new PlayerLimitReachedException("Nie ma więcej miejsc!");
        }
        if (room.getPassword() != null) {
            if (!passwordEncoder.matches(roomDto.getPassword(), room.getPassword().replace("{bcrypt}", ""))) {
                throw new PasswordsDontMatchException("Hasła nie są identyczne");
            }
        }
        room = roomService.addPlayer(room, player);
        log.info("Player with token: {}, joined room: {}", player.getToken(), room);
        return room;
    }

    @MessageMapping("/room/{roomCode}/begin")
    @SendTo("/room/{roomCode}/beginGame")
    public Room startGame(@DestinationVariable String roomCode) {
        Room room = roomRepo.findByCode(roomCode).orElseThrow();
        room.setPlaying(true);
        room.setPlayersVotedCount(0);
        Card card = getRandomCard("");
        room.setCurrentCard(card.getContent());
        return roomRepo.save(room);
    }

    @MessageMapping("/room/{roomCode}/next")
    @SendTo("/room/{roomCode}/card")
    public Room voteAndGetCard(@DestinationVariable String roomCode, @Payload String playerToken) throws MaxNumberOfVotesException {
        Room room = roomRepo.findByCode(roomCode).orElseThrow();
        Player player = userRepo.findByToken(playerToken).orElseThrow();

        if (player.isVoted()) {
            throw new MaxNumberOfVotesException("Nie możesz głosować dwa razy");
        }

        room = roomService.addVote(room, player);
        if (room.getPlayers().size() == room.getPlayersVotedCount()) {
            Card newCard = getRandomCard(room.getCurrentCard());
            room.setCurrentCard(newCard.getContent());
            room.zeroVotedCount();
            roomService.zeroVotes(room);
        }
        return roomRepo.save(room);
    }

    @MessageExceptionHandler
    @SendToUser("/queue/error")
    public String handleException(Exception e) throws Exception {
//        if(e instanceof MaxNumberOfVotesException || e instanceof PasswordsDontMatchException || e instanceof PlayerLimitReachedException)
//            return e.getMessage();
//        else throw e;
        return e.getMessage();
    }

    @org.springframework.context.event.EventListener
    public void handleUnsubscribe(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String playerToken = accessor.getSessionAttributes().get("playerToken").toString();
        Player player = userRepo.findByToken(playerToken).orElseThrow();
        Optional<Room> roomOpt = roomRepo.findByPlayers_Token(playerToken);

        if (roomOpt.isPresent() && player.isInRoom()) {
            Room room = roomOpt.get();
            room = roomService.removePlayer(room, player);

            if (room.getPlayers().isEmpty()) {
                log.info("RoomCode: {}, isKeepAlive: {}", room.getCode(), room.isKeepAlive() );
                if (!(room.isKeepAlive())) {
                    roomRepo.delete(room);
                    log.info("Deleted room: " + room.getCode());
                } else {
                    room.setHostToken(null);
                    room.setPlaying(false);
                }
            } else {
                if (room.getHostToken().equals(playerToken)){
                    room.setHostToken(room.getPlayers().get(new SecureRandom().nextInt(room.getPlayers().size())).getToken());
                }
                room = roomRepo.save(room);
                template.convertAndSend("/room/" + room.getCode() + "/join", room);
            }
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Player player = userRepo.findByToken(accessor.getSessionAttributes().get("playerToken").toString()).orElseThrow();
        userRepo.delete(player);
        log.info("Disconnected player: {}", player);
    }


    private String generateToken(Player player) {
        String token = passwordEncoder.encode(player.getName() + player.getId() + System.currentTimeMillis());
        player.setToken(token);
        userRepo.save(player);
        return token;
    }

    private Card getRandomCard(String currentCard) {
        List<Card> allCards = cardRepo.findAll();
        List<Card> weightedCard = new ArrayList<>();
        if (allCards.isEmpty()) return null;

        allCards = allCards
                .stream()
                .filter(c -> !c.getContent().equals(currentCard))
                .toList();

        for (Card card : allCards) {
            for (int i = 0; i < card.getRarity(); i++) {
                weightedCard.add(card);
            }
        }

        return weightedCard.get(new SecureRandom().nextInt(weightedCard.size()));
    }
}
