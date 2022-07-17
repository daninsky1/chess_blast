package com.example.chessblast.service;

import com.example.chessblast.game.GameRepository;
import com.example.chessblast.service.exceptions.EmailAlreadyExistException;
import com.example.chessblast.service.exceptions.PlayerIsPlayingAnotherGame;
import com.example.chessblast.service.exceptions.UserAlreadyExistException;
import com.example.chessblast.user.User;
import com.example.chessblast.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    private final List<User> users;
    private final String[][] anandVsKasparovMoves = {    // Game Moves Notation
        {"e4", "c5"}, {"Nf3", "d6"}, {"cxd4", "Nxd4"}, {"Nf6 ", "Nc3"},
        {"a6", "Be3"}, {"Ng4", "Bg5"}, {"h6", "Bh4"}, {"g5 ", "Bg3"},
        {"Bg7 ", "Be2"}, {"h5", "Bxg4"}, {"Bxg4", "f3"}, {"Bd7 ", "Bf2"},
        {"Nc6", "Qd2"}, {"Ne5 ", "O-O"}, {"g4 ", "f4"}, {"Nc4 ", "Qe2"},
        {"Rc8 ", "b3"}, {"Na3 ", "Nd5"}, {"e6", "Nb4"}, {"Qa5", "Qe1"},
        {"h4", "Be3"}, {"h3", "g3"}, {"Nb5", "Rd1"}, {"Nc3", "Nd3"},
        {"Qc7 ", "Rc1"}, {"Nxe4", "f5"}, {"e5", "f6"}, {"Nxf6", "Nf5"},
        {"Bxf5", "Rxf5"}, {"Qc6", "Qe2"}, {"Qe4", "Rf2"}, {"Nd5", "Re1"},
        {"Qxe3", "Qxg4"}, {"O-O", "Rxe3"}, {"Nxe3", "Qxh3"}, {"Nxc2", "Qd7"},
        {"Nd4", "Qxb7"}, {"a5", "Kg2"}, {"Rc3", "Nb2"}, {"Nc2", "Nc4"},
        {"d5", "Nd6"}, {"Ne3+", "Kh3"}, {"f5", "Qd7"}, {"f4+", "Qe6+"},
        {"Kh7", "Nf7"}, {"Rxf7", "Qxf7"}, {"Rc6", "gxf4"}, {"Rf6", "Qc7"},
        {"e4", "f5"}, {"d4", "Qe7"}, {"Rh6+", "Kg3"}, {"Nd1", "Rf4"},
        {"e3", "Rg4"}
    };

    private final String[][] carlsenVsViditMoves = {     // Game Moves Notation
        {"e4", "e5"}, {"Nf3", "Nc6"}, {"Bc4", "Bc5"}, {"d4", "exd4"},
        {"Ng5"}
    };

    public UserServiceTest() {
        users = Arrays.asList(
            new User("carlsen", "magnus@carlsen.com", "password"),
            new User("vidit", "vidit@gujrathi.com", "password"),
            new User("anand", "viswanathan@anand.com", "password"),
            new User("kasparov", "garry@kasparov.com", "password")
        );
    }

    @Test
    void signUpUser() throws UserAlreadyExistException, EmailAlreadyExistException {
        for (User user : users) {
            userService.signUpUser(user);
        }
    }

    @Test
    void createGame() throws PlayerIsPlayingAnotherGame {
        User carlsen = userService.getUserByUsername("carlsen").get();
        User vidit = userService.getUserByUsername("vidit").get();

        User anand = userService.getUserByUsername("anand").get();
        User kasparov = userService.getUserByUsername("kasparov").get();

        try {
            userService.createGame(carlsen, vidit);
            userService.createGame(anand, kasparov);
        }
        catch (PlayerIsPlayingAnotherGame e) {
            fail("Should not throw UserAlreadyExistException");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail("Should not throw any Exception");
        }
    }

    @Test
    void playMove() {
        User carlsen = userService.getUserByUsername("carlsen").get();
        User vidit = userService.getUserByUsername("vidit").get();

        User anand = userService.getUserByUsername("anand").get();
        User kasparov = userService.getUserByUsername("kasparov").get();

        for (String[] move : anandVsKasparovMoves) {
            userService.playMove(anand, anand.getActiveGame(), move[0]);
            if (move.length == 2) {
                userService.playMove(kasparov, kasparov.getActiveGame(), move[1]);
            }
        }
        for (String[] move : carlsenVsViditMoves) {
            userService.playMove(carlsen, anand.getActiveGame(), move[0]);
            if (move.length == 2) {
                userService.playMove(vidit, kasparov.getActiveGame(), move[1]);
            }
        }
    }
//
    @Test
    void resignGame() {
        User kasparov = userService.getUserByUsername("kasparov").get();
        userService.resignGame(kasparov);
    }
}