package com.rest.api.example.domain.request;

/**
 * Created by vbarros on 16/09/2019 .
 */
public class CompanyRequest extends BaseRequest {

    private String name;
    private String website;
    private Long Id;

    public CompanyRequest() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
