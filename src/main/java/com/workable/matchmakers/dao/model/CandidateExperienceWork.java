package com.workable.matchmakers.dao.model;

import lombok.Getter;
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
@Table(name = "CANDIDATE_EXPERIENCE_WORK")
@Getter
@Setter
public class CandidateExperienceWork extends ResourceBase {

    private String role;

    private String company;

    private Long yearStart;

    private Long yearEnd;

    @ManyToOne
    @JoinColumn(name = "CANDIDATE_EXPERIENCE_ID")
    private CandidateExperience candidateExperience;

}
