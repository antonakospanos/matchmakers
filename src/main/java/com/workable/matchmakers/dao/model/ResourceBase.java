package com.workable.matchmakers.dao.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
@Setter
@OptimisticLocking(type = OptimisticLockType.VERSION) // Optimistic locking using Entity's @Version column
public abstract class ResourceBase implements Serializable {

    // Surrogate primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Column used by hibernate for transaction's optimistic locking
     */
    @Version
    ZonedDateTime createdAt;
    // Long version;
}
