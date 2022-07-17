package com.example.chessblast.service;

import com.example.chessblast.game.Game;
import com.example.chessblast.game.GameRepository;
import com.example.chessblast.game.GameResult;
import com.example.chessblast.move.Move;
import com.example.chessblast.service.exceptions.EmailAlreadyExistException;
import com.example.chessblast.service.exceptions.PlayerIsPlayingAnotherGame;
import com.example.chessblast.service.exceptions.UserAlreadyExistException;
import com.example.chessblast.user.User;
import com.example.chessblast.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "User: \"%s\" not found.";
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username))
        );
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void signUpUser(User user) throws UserAlreadyExistException, EmailAlreadyExistException {
        boolean userExist = userRepository.findByUsername(user.getUsername()).isPresent();
        boolean emailExist = userRepository.findByUsername(user.getEmail()).isPresent();

        if (userExist) {
            throw new UserAlreadyExistException("Username already taken!");
        }
        else if (emailExist) {
            throw new EmailAlreadyExistException("Email already taken!");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }

    public void createGame(User whitePlayer, User blackPlayer) throws PlayerIsPlayingAnotherGame {
        if (whitePlayer.getActiveGame() != null) throw new PlayerIsPlayingAnotherGame(
            "Player " + whitePlayer.getUsername() + " is playing game "
                + gameRepository.findByWhitePlayer(whitePlayer).get().getId()
        );
        if (blackPlayer.getActiveGame() != null) throw new PlayerIsPlayingAnotherGame(
            "Player " + blackPlayer.getUsername() + " is playing game "
                + gameRepository.findByBlackPlayer(blackPlayer).get().getId()
        );
        Game game = new Game(whitePlayer, blackPlayer);
        gameRepository.save(game);

        whitePlayer.setActiveGame(game);
        blackPlayer.setActiveGame(game);
        userRepository.save(whitePlayer);
        userRepository.save(blackPlayer);
    }

    public void playMove(User player, Game game, String standardNotation) {
        Move move = new Move(game, standardNotation);
    }

    public void resignGame(User playerWhoResigned) {
        Game gameToResign = playerWhoResigned.getActiveGame();
        Move resign;
        playerWhoResigned.setActiveGame(null);
        User opponentPlayer;
        if (playerWhoResigned == gameToResign.getWhitePlayer()) {
            resign = new Move(gameToResign, "0-1");
            gameToResign.setResult(GameResult.BLACK_WIN);
            opponentPlayer = gameToResign.getBlackPlayer();
            opponentPlayer.setActiveGame(null);
        }
        else {
            resign = new Move(gameToResign, "1-0");
            gameToResign.setResult(GameResult.WHITE_WIN);
            opponentPlayer = gameToResign.getWhitePlayer();
            opponentPlayer.setActiveGame(null);
        }
        gameRepository.save(gameToResign);
        userRepository.save(playerWhoResigned);
        userRepository.save(opponentPlayer);
    }

    public void drawGame(Game gameToDraw) {
        Move draw = new Move(gameToDraw, "1/2-1/2");
        gameToDraw.setResult(GameResult.DRAW);
        User whitePlayer = gameToDraw.getWhitePlayer();
        User blackPlayer = gameToDraw.getBlackPlayer();
        whitePlayer.setActiveGame(null);
        blackPlayer.setActiveGame(null);

        gameRepository.save(gameToDraw);
        userRepository.save(whitePlayer);
        userRepository.save(blackPlayer);
    }
}
