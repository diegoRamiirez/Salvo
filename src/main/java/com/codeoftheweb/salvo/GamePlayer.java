package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvos = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships = new HashSet<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;


    public GamePlayer() {

    }

    public GamePlayer(Game game, Player player) {
        this.date = LocalDateTime.now();
        this.game = game;
        this.player = player;
    }



    public LocalDateTime getDate() {
        return date;
    }

    public Map<String , Object > toDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.id);
        dto.put("player", this.player.toDTOPlayer());
        Score score = this.getScore();
        if (score != null ) {
            dto.put("score",score.toDTOScore());
        }
        else {
            dto.put("score",null);
        }
        return dto;
    }

    public Map<String , Object > toGameViewDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.id);
        dto.put("gamePlayers", this.game.getGamePlayers().stream().map(GamePlayer::toDTO).collect(toList()));
        dto.put("ships", this.ships.stream().map(Ship::toDTOShips).collect(toList()));
        dto.put("salvos", this.game.getGamePlayers().stream().flatMap(gamePlayer -> gamePlayer.getSalvo().stream().map(Salvo::toDTOSalvos)).collect(toList()));
        return dto;
    }




    public Set<Ship> getShips() {
        return ships;
    }

    public void addSalvo (Salvo salvo) {
        salvo.setGamePlayer(this);
        salvos.add(salvo);
    }

    public void addShip(Ship ship) {
        ship.setGamePlayer(this);
        ships.add(ship);
    }

    public Score getScore() {
        return this.player.getScore(this.game);
    }
    public Set<Salvo> getSalvo() {
        return salvos;
    }


    public long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }


}


