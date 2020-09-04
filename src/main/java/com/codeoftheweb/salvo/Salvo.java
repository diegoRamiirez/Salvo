package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int turno;
    private int jugador;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;


    @ElementCollection
    @Column(name = "SalvoLocation")
    private List<String> salvoLocations = new ArrayList<>();


    public Salvo() {

    }


    public Map<String, Object> toDTOSalvos() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turno", this.turno);
        dto.put("salvoLocations", this.salvoLocations);
        dto.put("playerId", this.gamePlayer.getPlayer().getId());
        return dto;
    }

    

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public int getJugador() {
        return jugador;
    }

    public Salvo(long id) {
        this.id = id;
    }

    public Salvo(int turno, int jugador, List<String> salvoLocations) {
        this.turno = turno;
        this.salvoLocations = salvoLocations;
        this.jugador = jugador;

    }

    public int getTurno() {
        return turno;
    }

    public long getId() {
        return id;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void add(Salvo salvo) {
    }
}
