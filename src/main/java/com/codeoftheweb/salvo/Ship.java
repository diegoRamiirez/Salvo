package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Ship {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
   @GenericGenerator(name = "native", strategy = "native")
   private long id;

   private String type;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "gamePlayer_id")
   private GamePlayer gamePlayer ;

   @ElementCollection
   @Column(name="ShipLocation")
   private List<String> locations = new ArrayList<>();

   public Ship (){

   }

   public Map<String, Object > toDTOShips() {
      Map<String, Object> dto = new LinkedHashMap<>();
      dto.put("type", this.type);
      dto.put("locations", this.locations);
      return dto;
   }

   public Ship (String type, List<String> locations){
      this.type = type;
      this.locations = locations;
   }

   public void setGamePlayer(GamePlayer gamePlayer) { this.gamePlayer = gamePlayer; }

   public String getType() {
      return type;
   }

   public GamePlayer getGamePlayer() {
      return gamePlayer;
   }

   public List<String> getLocations() {
      return locations;
   }

   public long getId() {
      return id;
   }
}
