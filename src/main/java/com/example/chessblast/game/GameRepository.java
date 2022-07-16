package com.example.chessblast.game;

import com.example.chessblast.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.function.Supplier;


@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findById(long id);
    Optional<Game> findByWhitePlayer(User user);
    Optional<Game> findByBlackPlayer(User user);
}
