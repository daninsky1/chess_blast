package com.example.chessblast.game;

import com.example.chessblast.move.Move;
import com.example.chessblast.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;


@Entity(name = "Game")
@Table(name = "games")
@NoArgsConstructor
@Getter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
        name = "white_player_id", nullable = false,
        foreignKey = @ForeignKey(name = "FK_white_player_game")
    )
    private User whitePlayer;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(
        name = "black_player_id", nullable = false,
        foreignKey = @ForeignKey(name = "FK_black_player_game")
    )
    private User blackPlayer;

    @Column(nullable = true)
    private GameResult result;

    @OneToMany(mappedBy = "game")
    Set<Move> moves;

    public Game(User whitePlayer, User blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    public GameResult getResult() {
        return result;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }
}
