package com.rest.api.example.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by vbarros on 16/09/2019 .
 */
@Entity
@Table(name = "companies")
public class Company extends AbstractPersistable<Long>  {

    @Column(name = "name")
    private String name;

    @Column(name = "website")
    private String website;

    @Column
    private boolean deleted;

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
