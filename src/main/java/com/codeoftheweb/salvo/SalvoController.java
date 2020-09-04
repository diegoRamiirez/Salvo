package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    public Map<String, Object> Game(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (isGuest(authentication)) {
            dto.put("playerLog", null);
        } else {
            Player playerLog = playerRepository.findByUserName(authentication.getName());
            dto.put("playerLog", playerLog.toDTOPlayer());
        }
        dto.put("games",
                gameRepository
                        .findAll()
                        .stream()
                        .map(Game::toDTO)
                        .collect(Collectors.toList()));

        return dto;
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> register(@RequestParam String userName, @RequestParam String password) {

        if (!(userName.contains("@")) || !userName.contains(".") || userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("401", "Please enter a valid e-mail/password"), HttpStatus.FORBIDDEN);
        }

        Player player = playerRepository.findByUserName(userName);
        if (player != null) {
            return new ResponseEntity<>(makeMap("403", "Username already exists"), HttpStatus.CONFLICT);
        }

        Player newPlayer = playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(newPlayer.toDTOPlayer(), HttpStatus.CREATED);

    }


    @GetMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> gameView(@PathVariable Long gamePlayerId, Authentication authentication) {
        Player playerLog = playerRepository.findByUserName(authentication.getName());
        Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
        if (!gamePlayer.isPresent()) {
            return new ResponseEntity<>(makeMap("error", "This game has not been found."), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.get().getPlayer().getId() != playerLog.getId()) {
            return new ResponseEntity<>(makeMap("error", "You are not authorized to see this info."), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(gamePlayer.get().toGameViewDTO(), HttpStatus.OK);
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No player logged. Please Log in before entering any game"), HttpStatus.FORBIDDEN);
        }
        Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
        Player player = playerRepository.findByUserName(authentication.getName());
        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(newGame, player));

        return new ResponseEntity<>(makeMap("gamePlayerId", newGamePlayer.getId()), HttpStatus.CREATED);
    }


 /* @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity <Map<String,Object>> joinGame(@PathVariable Long gameId, Authentication authentication )  {
​
        Player player = playerRepository.findByUserName ( authentication.getName());
        GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer( player, game.get()));
​
        return new ResponseEntity <> (makeMap ("gpid", newGamePlayer.getId()), HttpStatus.CREATED);
​
    }

    Player player = playerRepository.findByUserName ( authentication.getName ());
        if (gamePlayer.get ().getPlayer ().getId () != player.getId ()){
            return new ResponseEntity<>(makeMap ("error", "You are not authorized to see this info."), HttpStatus.UNAUTHORIZED);
        }
*/

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }


    @PostMapping("/games/{gameId}/player")
    private ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameId, Authentication authentication) {
        Player player = playerRepository.findByUserName(authentication.getName());
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "Not logged in!"), HttpStatus.UNAUTHORIZED);
        }

        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            if (game.getGamePlayers().size() > 1) {
                return new ResponseEntity<>(makeMap("error", "Already 2 players"), HttpStatus.FORBIDDEN);
            }
            if (game.getGamePlayers().stream().anyMatch(gamePlayer -> gamePlayer.getPlayer().getId() == player.getId())) {
                return new ResponseEntity<>(makeMap("error", "You can't play against yourself!"), HttpStatus.FORBIDDEN);
            }

            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player));
            return new ResponseEntity<>(makeMap("gpID", gamePlayer.getId()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(makeMap("error", "this game does not exist"), HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String, Object>> addShip(@PathVariable Long gamePlayerId, @RequestBody List<Ship> ships, Authentication authentication) {
        GamePlayer gameplayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
        Player player = playerRepository.findByUserName(authentication.getName());
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No player logged. Please Log in before entering any game"), HttpStatus.UNAUTHORIZED);
        } else if (gameplayer == null) {
            return new ResponseEntity<>(makeMap("error", "there is no game player with the given ID"), HttpStatus.NOT_FOUND);
        } else if (gameplayer.getPlayer().getId() != player.getId()) {
            return new ResponseEntity<>(makeMap("error", "this is not your game"), HttpStatus.UNAUTHORIZED);
        } else if (gameplayer.getShips().size() > 0) {
            return new ResponseEntity<>(makeMap("error", "Ships allready located"), HttpStatus.FORBIDDEN);
        } else {
            ships.forEach(ship -> gameplayer.addShip(ship));
            gamePlayerRepository.save(gameplayer);
            return new ResponseEntity<>(makeMap("succes", "ships created succesfully"), HttpStatus.FORBIDDEN);
        }
    }
}






