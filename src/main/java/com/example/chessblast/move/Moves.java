package com.example.chessblast.move;

import com.example.chessblast.game.Game;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name = "Move")
@Table(name = "moves")
@NoArgsConstructor
public class Moves {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "game_id", nullable = false,
        foreignKey = @ForeignKey(name = "FK_game_move")
    )
    private Game game;

    @Column(
        nullable = false,
        length = 64
    )
    private String pgnMoveNotation;

    public Moves(Game game, String pgnMoveNotation) {
        this.game = game;
        this.pgnMoveNotation = pgnMoveNotation;
    }
}
