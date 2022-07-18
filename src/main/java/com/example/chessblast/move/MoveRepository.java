package com.example.chessblast.move;

import com.example.chessblast.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MoveRepository extends JpaRepository<Move, Long> {
    List<Move> findByGameOrderById(Game game);
}
