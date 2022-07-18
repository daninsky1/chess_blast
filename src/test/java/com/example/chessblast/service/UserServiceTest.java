package com.example.chessblast.service;

import com.example.chessblast.game.Game;
import com.example.chessblast.game.GameRepository;
import com.example.chessblast.service.exceptions.EmailAlreadyExistException;
import com.example.chessblast.service.exceptions.PlayerIsPlayingAnotherGame;
import com.example.chessblast.service.exceptions.UserAlreadyExistException;
import com.example.chessblast.user.User;
import com.example.chessblast.user.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
            new User("anand", "viswanathan@anand.com", "password"),
            new User("kasparov", "garry@kasparov.com", "password"),
            new User("carlsen", "magnus@carlsen.com", "password"),
            new User("vidit", "vidit@gujrathi.com", "password")
        );
    }

    @Test
    @Order(1)
    void signUpUser() throws UserAlreadyExistException, EmailAlreadyExistException {
        for (User user : users) {
            userService.signUpUser(user);
        }
        for (User user : users) {
            userService.getUserByUsername(user.getUsername());
//            assertEquals();
        }
    }

    @Test
    @Order(2)
    void createGame() throws PlayerIsPlayingAnotherGame {
        User anand = userService.getUserByUsername("anand").get();
        User kasparov = userService.getUserByUsername("kasparov").get();

        User carlsen = userService.getUserByUsername("carlsen").get();
        User vidit = userService.getUserByUsername("vidit").get();

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
    @Order(3)
    void playMove() {
        User anand = userService.getUserByUsername("anand").get();
        User kasparov = userService.getUserByUsername("kasparov").get();
        for (String[] move : anandVsKasparovMoves) {
            userService.addPlayerMove(anand, move[0]);
            if (move.length == 2) {
                userService.addPlayerMove(kasparov, move[1]);
            }
        }

        User carlsen = userService.getUserByUsername("carlsen").get();
        User vidit = userService.getUserByUsername("vidit").get();
        for (String[] move : carlsenVsViditMoves) {
            userService.addPlayerMove(carlsen, move[0]);
            if (move.length == 2) {
                userService.addPlayerMove(vidit, move[1]);
            }
        }
    }
//
    @Test
    @Order(4)
    void resignGame() {
        User kasparov = userService.getUserByUsername("kasparov").get();
        userService.resignGame(kasparov);
    }

    @Test
    @Order(5)
    void drawGame() {
        User carlsen = userService.getUserByUsername("carlsen").get();
        Game game = carlsen.getActiveGame();
        userService.drawGame(game);
    }
}