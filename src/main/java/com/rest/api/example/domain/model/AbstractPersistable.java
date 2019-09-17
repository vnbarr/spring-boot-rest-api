package com.rest.api.example.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 *
 * @param <PK>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractPersistable<PK extends Serializable> implements Persistable<PK> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private PK id;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isNew() {
        return id == null;
    }

    //@Version
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column(name = "updated")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updated;

    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(name = "created")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date created;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
