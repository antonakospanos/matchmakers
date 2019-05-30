package com.workable.matchmakers.dao.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@Cacheable
@DynamicUpdate
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matchmakers.entity-cache")
public class User extends ResourceBase {

    // Exposed resource ID
    private UUID externalId;

    private String username;

    /**
     * Hashed password
     */
    private String password;

    private String name;

    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="IMAGE_ID")
    private Blob image;

    public User() {
        this.externalId = UUID.randomUUID();
    }
}
