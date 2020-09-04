package com.codeoftheweb.salvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class);
	}

@Autowired
PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository , GameRepository gameRepository , GamePlayerRepository gamePlayerRepository , ShipRepository shipRepository , SalvoRepository salvoRepository,ScoreRepository scoreRepository ) {
		return (args) -> {
			// save a couple of customers
			Player player1 = new Player ("j.bauer@ctu.gov",passwordEncoder.encode("24"));
			Player player2 = new Player("c.obrian@ctu.gov",passwordEncoder.encode("42"));
			Player player3 = new Player ("kim_bauer@gmail.com",passwordEncoder.encode("kb"));
			Player player4 = new Player ("t.almeida@ctu.gov",passwordEncoder.encode("mole"));

			Game game1 = new Game (LocalDateTime.now());
			Game game2 = new Game (LocalDateTime.now().plusHours(1));
			Game game3 = new Game (LocalDateTime.now().plusHours(2));
			Game game4 = new Game (LocalDateTime.now().plusHours(3));
			Game game5 = new Game (LocalDateTime.now().plusHours(4));
			Game game6 = new Game (LocalDateTime.now().plusHours(5));

			Ship ship1 = new Ship ("Destroyer", Arrays.asList("H2", "H3", "H4"));
			Ship ship2 = new Ship ("Submarine" , Arrays.asList("E1", "F1", "G1"));
			Ship ship3 = new Ship ("Patrol boat", Arrays.asList("B4","B5"));
			Ship ship4 = new Ship ("Destroyer" , Arrays.asList("B5", "C5", "D5"));
			Ship ship5 = new Ship ("Patrol boat", Arrays.asList("F1","F2"));
			Ship ship6 = new Ship ("destructor" , Arrays.asList("B5", "C5", "D5"));
			Ship ship7 = new Ship ("Patrol boat", Arrays.asList("C6","C7"));
			Ship ship8 = new Ship ("destructor" , Arrays.asList("F10", "F9", "F8"));
			Ship ship9 = new Ship ("cruiser", Arrays.asList("B6","B7","B8"));
			Ship ship10 = new Ship ("destructor" , Arrays.asList("C1", "C2", "C3"));
			Ship ship11 = new Ship ("cruiser", Arrays.asList("B4","B5","B6"));

			Salvo salvo1 = new Salvo ( 1 ,1,Arrays.asList("B5", "C5", "F1"));
			Salvo salvo2 = new Salvo (1,2,Arrays.asList("B4", "B5", "B6"));
			Salvo salvo3 = new Salvo (2 , 1,Arrays.asList("F2", "D5" ));
			Salvo salvo4 = new Salvo (2 , 2,Arrays.asList("E1", "H3", "A2"));

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);

			GamePlayer gamePlayer1 = new GamePlayer(game1,player1);
			GamePlayer gamePlayer2 = new GamePlayer(game1,player2);
			GamePlayer gamePlayer3 = new GamePlayer(game2,player1);
			GamePlayer gamePlayer4 = new GamePlayer(game2,player2);
			GamePlayer gamePlayer5 = new GamePlayer(game3,player2);
			GamePlayer gamePlayer6 = new GamePlayer(game3,player4);
			GamePlayer gamePlayer7 = new GamePlayer(game4,player1);
			GamePlayer gamePlayer8 = new GamePlayer(game4,player2);
			GamePlayer gamePlayer9 = new GamePlayer(game5,player4);
			GamePlayer gamePlayer10 = new GamePlayer(game5,player1);
			GamePlayer gamePlayer11 = new GamePlayer(game6,player4);


			gamePlayer1.addShip(ship1);
			gamePlayer1.addShip(ship2);
			gamePlayer1.addShip(ship3);
			gamePlayer2.addShip(ship4);
			gamePlayer2.addShip(ship5);
			gamePlayer6.addShip(ship6);
			gamePlayer7.addShip(ship7);
			gamePlayer8.addShip(ship8);
			gamePlayer9.addShip(ship9);
			gamePlayer10.addShip(ship10);
			gamePlayer11.addShip(ship11);

			gamePlayer1.addSalvo(salvo1);
			gamePlayer2.addSalvo(salvo2);
			gamePlayer1.addSalvo(salvo3);
			gamePlayer2.addSalvo(salvo4);


			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);
			gamePlayerRepository.save(gamePlayer8);
			gamePlayerRepository.save(gamePlayer9);
			gamePlayerRepository.save(gamePlayer10);
			gamePlayerRepository.save(gamePlayer11);


			Score score1 = new Score(1, game1 , player1, LocalDateTime.now().plusMinutes(30));
			scoreRepository.save(score1);
			Score score2 = new Score(0, game1 , player2,LocalDateTime.now().plusMinutes(30));
			scoreRepository.save(score2);
			Score score3 = new Score(0.5, game2 , player1,LocalDateTime.now().plusMinutes(30));
			scoreRepository.save(score3);
			Score score4 = new Score(0.5, game2 , player2, LocalDateTime.now().plusMinutes(30));
			scoreRepository.save(score4);
			Score score5 = new Score(1, game3 , player2,LocalDateTime.now().plusMinutes(30));
			scoreRepository.save(score5);
			Score score6 = new Score(0, game3 , player4, LocalDateTime.now().plusMinutes(30));
			scoreRepository.save(score6);
			Score score7 = new Score(0.5, game4 , player2, LocalDateTime.now().plusMinutes(30));
			scoreRepository.save(score7);
			Score score8 = new Score(0.5, game4 , player1, LocalDateTime.now().plusMinutes(30));
			scoreRepository.save(score8);





		};
	}
}



@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}




	@Autowired
	PlayerRepository playerRepository;


	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputPlayer-> {
			Player player = playerRepository.findByUserName(inputPlayer);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputPlayer);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()

			//	.antMatchers("/api/games","/web/games.html","/web/css/style.css","/web/js/games.js").permitAll()
			//	.antMatchers("/**").hasAuthority("USER")
				.antMatchers("/web/game.html", "/api/game_view/**").hasAuthority("USER")
				.antMatchers("/**").permitAll()
				.and()
				.formLogin();
		http.formLogin()
				.usernameParameter("userName")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");

			http.csrf().disable();

			// if user is not authenticated, just send an authentication failure response
			http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

			// if login is successful, just clear the flags asking for authentication
			http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

			// if login fails, just send an authentication failure response
			http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

			// if logout is successful, just send a success response
			http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
		}

		private void clearAuthenticationAttributes(HttpServletRequest request) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			}
		}

	}
