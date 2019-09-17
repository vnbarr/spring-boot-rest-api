package com.rest.api.example.exception;

/**
 * Created by vbarros on 16/09/2019 .
 */
public class MissingParameterException extends Exception {
    private String entity;
    private String field;

    public MissingParameterException(String entity, String field) {
        super("Missing parameter: "+ field + " on "+entity);
        this.field = field;
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
