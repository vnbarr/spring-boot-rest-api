package com.rest.api.example.exception;

/**
 * Created by vbarros on 16/09/2019 .
 */
public class DataConflictException extends Exception {
    private String entity;

    public DataConflictException(String entity) {
        super("Data conflict with entity: "+entity);
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }
}
