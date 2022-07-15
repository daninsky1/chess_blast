package com.example.chessblast.game;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GameRepositoryTest {
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    GameRepository gameRepository;
//
//    List<Game> games;
//    List<User> users;
//
//    public GameRepositoryTest() {
//        users = Arrays.asList(
//            new User("daniel", "dan@sky.com", "1234"),
//            new User("marina", "mari@sky.com", "1234"),
//            new User("jose", "jose@sky.com", "1234"),
//            new User("zilda", "zilda@sky.com", "1234")
//        );
//        userRepository.saveAll(users);
//
//        games = Arrays.asList(
//            new Game(users.get(0), users.get(1)),
//            new Game(users.get(1), users.get(0)),
//            new Game(users.get(2), users.get(3)),
//            new Game(users.get(3), users.get(2))
//        );
//    }
//
//    @Test
//    void saveGame() {
//        gameRepository.saveAll(games);
//
//        for (Game game : games) {
//            assertEquals(game, gameRepository.findById(game.getId()));
//        }
//    }
//
//    @Test
//    void findById() {
//        gameRepository.saveAll(games);
//        for (Game game : games) {
//            Game savedGame = gameRepository.save(game);
//        }
//    }
}