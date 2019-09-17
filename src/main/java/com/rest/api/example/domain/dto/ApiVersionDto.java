package com.rest.api.example.domain.dto;

/**
 * Created by vbarros on 16/09/2019.
 */
public class ApiVersionDto {
    private String version;

    public ApiVersionDto(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
