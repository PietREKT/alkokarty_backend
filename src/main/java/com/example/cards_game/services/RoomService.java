package com.example.cards_game.services;

import com.example.cards_game.data.RoomRepository;
import com.example.cards_game.data.UserRepository;
import com.example.cards_game.models.Player;
import com.example.cards_game.models.Room;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final UserRepository userRepo;
    private final RoomRepository roomRepo;

    public RoomService(UserRepository userRepo, RoomRepository roomRepo) {
        this.userRepo = userRepo;
        this.roomRepo = roomRepo;
    }

    public Room addPlayer(Room r, Player p){
        r.getPlayers().add(p);
        p.setRoom(r);
        r = roomRepo.save(r);
        userRepo.save(p);
        return r;
    }

    public Room addVote(Room r, Player p){
        p.setVoted(true);
        r.getPlayers().set(indexOf(r.getPlayers(), p), p);
        r.incVotedCount();
        return roomRepo.save(r);
    }

    private int indexOf(List<Player> players, Player p){
        int index = -1;
        for (int i = 0; i < players.size(); i++){
            if (players.get(i).equals(p)){
                index = i;
                break;
            }
        }
        return index;
    }

    public List<Player> getPlayersVoted(Room r) {
        return r.getPlayers().stream().filter(Player::isVoted).collect(Collectors.toList());
    }

    public void removeVote(Room r, Player p){
        int index = r.getPlayers().indexOf(p);
        p.setVoted(false);
        r.getPlayers().set(index, p);
        roomRepo.save(r);
        userRepo.save(p);
    }

    public void zeroVotes(Room r){
        r.getPlayers().forEach(p -> {
            p.setVoted(false);
            userRepo.save(p);
        });
        r.setPlayersVotedCount(0);
        roomRepo.save(r);
    }

    public Room removePlayer(Room r, Player p){
        removeFromList(r.getPlayers(), p);
        if (p.isVoted())
            r.decVotedCount();
        p.setRoom(null);
        p.setVoted(false);
        p.setInRoom(false);
        userRepo.save(p);
        return roomRepo.save(r);
    }

    private void removeFromList(List<Player> players, Player p){
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).equals(p)){
                players.remove(i);
                break;
            }
        }
    }
}
