package com.example.chessblast.game;

import com.example.chessblast.move.Move;
import com.example.chessblast.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity(name = "Game")
@Table(name = "games")
@NoArgsConstructor
@Getter
@Setter
public class Game {
    @Setter(AccessLevel.NONE)
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

    @Column(name = "result", nullable = true)
    private GameResult result;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "game")
    List<Move> moves;

    public Game(User whitePlayer, User blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }
}
