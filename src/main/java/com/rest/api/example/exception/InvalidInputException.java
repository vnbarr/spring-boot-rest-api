package com.rest.api.example.exception;

public class InvalidInputException extends Exception {
    private String keyWord;

    public InvalidInputException(String message, String keyWord) {
        super(message);
        this.keyWord = keyWord;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
