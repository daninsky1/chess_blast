package com.example.chessblast.move;

import com.example.chessblast.game.Game;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity(name = "Move")
@Table(name = "moves")
@NoArgsConstructor
@Getter
@Setter
public class Move {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "game_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_game_move")
    )
    private Game game;

    @Column(name = "white_move", nullable = false, length = 32)
    private String whiteMove;   // Chess standard notation

    @Column(name = "black_move", nullable = true, length = 32)
    private String blackMove;   // Chess standard notation

    public Move(Game game, String whiteMove) {
        this.game = game;
        this.whiteMove = whiteMove;
        this.blackMove = "";
        game.getMoves().add(this);
    }
}
