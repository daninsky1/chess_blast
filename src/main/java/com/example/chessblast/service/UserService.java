package com.example.chessblast.service;

import com.example.chessblast.game.Game;
import com.example.chessblast.game.GameRepository;
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
        if (whitePlayer.getPlayingNow() != null) throw new PlayerIsPlayingAnotherGame(
            "Player " + whitePlayer.getUsername() + " is playing game "
                + gameRepository.findByWhitePlayer(whitePlayer).get().getId()
        );
        if (blackPlayer.getPlayingNow() != null) throw new PlayerIsPlayingAnotherGame(
            "Player " + blackPlayer.getUsername() + " is playing game "
                + gameRepository.findByBlackPlayer(blackPlayer).get().getId()
        );

        Game game = new Game(whitePlayer, blackPlayer);
        gameRepository.save(game);
    }

    public void playMove(User player, Game game, String pgnMoveNotation) {
        Move move = new Move(game, pgnMoveNotation);
    }

    public void callGameOver(Game game, String result) {

    }
}
