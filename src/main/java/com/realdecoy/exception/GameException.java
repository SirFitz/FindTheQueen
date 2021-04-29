package com.realdecoy.exception;

public class GameException extends Exception {
    private  String message;

    public GameException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
