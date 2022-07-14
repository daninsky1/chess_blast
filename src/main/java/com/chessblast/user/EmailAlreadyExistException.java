package com.chessblast.user;


public class EmailAlreadyExistException extends Exception {
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
