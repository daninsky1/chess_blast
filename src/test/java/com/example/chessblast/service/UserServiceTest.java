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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private MoveRepository moveRepository;

    private final List<User> users;
    private final String[][] anandVsGarryMoves = {    // Game Moves Notation
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
            new User("garry", "garry@kasparov.com", "password"),
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
        User garry = userService.getUserByUsername("garry").get();

        User carlsen = userService.getUserByUsername("carlsen").get();
        User vidit = userService.getUserByUsername("vidit").get();

        try {
            userService.createGame(anand, garry);
            userService.createGame(carlsen, vidit);
        }
        catch (PlayerIsPlayingAnotherGame e) {
            fail("Should not throw UserAlreadyExistException");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            fail("Should not throw any Exception");
        }
        assertNotNull(anand.getActiveGame());
        assertNotNull(garry.getActiveGame());
        assertEquals(anand.getActiveGame(), garry.getActiveGame());

        assertNotNull(carlsen.getActiveGame());
        assertNotNull(vidit.getActiveGame());
        assertEquals(carlsen.getActiveGame(), vidit.getActiveGame());
    }

    @Test
    @Order(3)
    void playMove() {
        User anand = userService.getUserByUsername("anand").get();
        User garry = userService.getUserByUsername("garry").get();
        for (String[] move : anandVsGarryMoves) {
            userService.addGameMove(anand, move[0]);
            if (move.length == 2) {
                userService.addGameMove(garry, move[1]);
            }
        }

        List<Move> anandVsGarryStoredMoves =
            moveRepository.findByGameOrderById(anand.getActiveGame());
        for (int i = 0; i < anandVsGarryMoves.length; i++) {
            assertEquals(anandVsGarryStoredMoves.get(i).getWhiteMove(), anandVsGarryMoves[i][0]);
            if (anandVsGarryMoves[i].length == 2) {
                assertEquals(anandVsGarryStoredMoves.get(i).getBlackMove(), anandVsGarryMoves[i][1]);
            }
        }

        User carlsen = userService.getUserByUsername("carlsen").get();
        User vidit = userService.getUserByUsername("vidit").get();
        for (String[] move : carlsenVsViditMoves) {
            userService.addGameMove(carlsen, move[0]);
            if (move.length == 2) {
                userService.addGameMove(vidit, move[1]);
            }
        }

        List<Move> carlsenVsViditStoredMoves =
            moveRepository.findByGameOrderById(carlsen.getActiveGame());
        for (int i = 0; i < carlsenVsViditMoves.length; i++) {
            assertEquals(carlsenVsViditStoredMoves.get(i).getWhiteMove(), carlsenVsViditMoves[i][0]);
            if (carlsenVsViditMoves[i].length == 2) {
                assertEquals(carlsenVsViditStoredMoves.get(i).getBlackMove(), carlsenVsViditMoves[i][1]);
            }
        }
    }

    //
    @Test
    @Order(4)
    void resignGame() {
        User garry = userService.getUserByUsername("garry").get();
        Game game = garry.getActiveGame();
        User garryOpponent = game.getWhitePlayer();     // Anand
        userService.resignGame(garry);

        assertNull(garry.getActiveGame());
        assertNull(garryOpponent.getActiveGame());
        assertEquals(game.getResult(), GameResult.WHITE_WON);
    }

    @Test
    @Order(5)
    void drawGame() {
        User carlsen = userService.getUserByUsername("carlsen").get();
        Game game = carlsen.getActiveGame();
        User carlsenOpponent = game.getBlackPlayer();   // Vidit
        userService.drawGame(game);

        assertNull(carlsen.getActiveGame());
        assertNull(carlsenOpponent.getActiveGame());
        assertEquals(game.getResult(), GameResult.DRAW);
    }
}