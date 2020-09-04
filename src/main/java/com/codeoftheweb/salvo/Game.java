package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime date;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<Score> score;


    public Game() {

    }


    public Game(LocalDateTime date) {
        this.date = date;
    }

    public Game(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Map<String , Object > toDTO() {
        Map<String , Object > dto = new LinkedHashMap<>();
        dto.put("id" , this.id);
        dto.put("created", this.date);
        dto.put("gamePlayers", this.gamePlayers.stream().map(GamePlayer::toDTO).collect(toList()));
        return dto;

    }



    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }
}



