package com.workable.matchmakers.dao.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matchmakers.entity-cache")
@Table(name = "BLOB")
@Getter
@Setter
@NoArgsConstructor
public class Blob extends ResourceBase {

    public Blob(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    private String name;

    private String type;

    @Column(length=16777215) // MEDIUMBLOB
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

}
