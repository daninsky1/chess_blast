package com.example.chessblast.move;

import com.example.chessblast.game.Game;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name = "Move")
@Table(name = "move")
@NoArgsConstructor
public class Move {
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
    private String standardNotation;

    public Move(Game game, String notation) {
        this.game = game;
        this.standardNotation = notation;
    }
}
