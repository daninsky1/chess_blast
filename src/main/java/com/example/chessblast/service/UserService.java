package com.example.chessblast.service;

import com.example.chessblast.game.Game;
import com.example.chessblast.game.GameRepository;
import com.example.chessblast.game.GameResult;
import com.example.chessblast.move.Move;
import com.example.chessblast.move.MoveRepository;
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

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "User: \"%s\" not found.";
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final MoveRepository moveRepository;
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

    public void addGameMove(User player, String standardNotation) {
        Game activeGame = player.getActiveGame();
        List<Move> moves = moveRepository.findByGameOrderById(activeGame);
        Move lastMove = moves.isEmpty() ? null : moves.get(moves.size()-1);

        if (!moves.isEmpty()) {
            if (!lastMove.getBlackMove().isEmpty()) {
                // Write white player move
                moveRepository.save(new Move(activeGame, standardNotation));
            }
            else {
                // Write black player move
                lastMove.setBlackMove(standardNotation);
                moveRepository.save(lastMove);
            }
        }
        else {
            // Write first move
            moveRepository.save(new Move(activeGame, standardNotation));
        }
    }

    public void resignGame(User playerWhoResigned) {
        Game gameToResign = playerWhoResigned.getActiveGame();
        playerWhoResigned.setActiveGame(null);
        User opponentPlayer;
        if (playerWhoResigned == gameToResign.getWhitePlayer()) {
            gameToResign.setResult(GameResult.BLACK_WON);
            opponentPlayer = gameToResign.getBlackPlayer();
            opponentPlayer.setActiveGame(null);
        }
        else {
            gameToResign.setResult(GameResult.WHITE_WON);
            opponentPlayer = gameToResign.getWhitePlayer();
            opponentPlayer.setActiveGame(null);
        }
        gameRepository.save(gameToResign);
        userRepository.save(playerWhoResigned);
        userRepository.save(opponentPlayer);
    }

    public void drawGame(Game gameToDraw) {
        gameToDraw.setResult(GameResult.DRAW);
        List<Move> gameMoves = moveRepository.findByGameOrderById(gameToDraw);
        gameRepository.save(gameToDraw);

        User whitePlayer = gameToDraw.getWhitePlayer();
        User blackPlayer = gameToDraw.getBlackPlayer();
        whitePlayer.setActiveGame(null);
        blackPlayer.setActiveGame(null);
        userRepository.save(whitePlayer);
        userRepository.save(blackPlayer);
    }
}
