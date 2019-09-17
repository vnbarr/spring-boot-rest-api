package com.rest.api.example.exception;

/**
 * Created by vbarros on 16/09/2019 .
 */
public class DuplicatedEntityException extends Exception {
    private String entity;

    public DuplicatedEntityException(String entity) {
        super("Duplicated entity for: "+entity);
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }
}
