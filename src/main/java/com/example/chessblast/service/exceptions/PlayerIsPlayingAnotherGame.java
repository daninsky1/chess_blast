package com.example.chessblast.service.exceptions;

public class PlayerIsPlayingAnotherGame extends Exception {
    public PlayerIsPlayingAnotherGame(String message) {
        super(message);
    }
}
