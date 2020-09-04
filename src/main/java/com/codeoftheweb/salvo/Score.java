package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private double score;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    private LocalDateTime finishDate;

    public Score() {

    }

    public double getScore() {
        return score;
    }

    public long getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }


    public Score(double score, Game game, Player player, LocalDateTime finishDate) {
        this.score = score;
        this.game = game;
        this.player = player;
        this.finishDate = finishDate;
    }


        public Map<String, Object> toDTOScore () {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id", this.id);
            dto.put("score", this.score);
            dto.put("finishDate", this.finishDate);
            return dto;

    }

}
