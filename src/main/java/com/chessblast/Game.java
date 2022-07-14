package com.chessblast;


import com.chessblast.user.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Game")
@Table(name = "games")
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(
        name = "white_player_id", nullable = false,
        foreignKey = @ForeignKey(name = "FK_user_game")
    )
    private User whitePlayerId;

    @Column(nullable = true)
    private String result;

    @ManyToMany
    @JoinTable(
        name = "participants",
        joinColumns = @JoinColumn(name = "game_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"),
        foreignKey = @ForeignKey(name = "FK_game_participant"),
        inverseForeignKey = @ForeignKey(name = "FK_player_participant")
    )
    Set<User> participants;

    public Game(User whitePlayerId, String result) {
        this.whitePlayerId = whitePlayerId;
        this.result = result;
    }
}
