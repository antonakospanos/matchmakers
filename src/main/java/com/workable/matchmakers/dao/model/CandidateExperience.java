package com.workable.matchmakers.dao.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matchmakers.entity-cache")
@Table(name = "CANDIDATE_EXPERIENCE")
@Getter
@Setter
public class CandidateExperience extends ResourceBase {

    private Long years;

    private Boolean seniorManagement;

    private String degreeLevel;

    private String degreeCategory;

    private Boolean extraCurriculumActivities;

    @ElementCollection
    private Set<String> languages;

    @ElementCollection
    private Set<String> industries;

    @ElementCollection
    private Set<String> specializations;

    @ElementCollection
    private Set<String> skills;

    @ElementCollection
    private Set<String> customers;

    @OneToMany(mappedBy = "candidateExperience", cascade = CascadeType.ALL , fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<CandidateExperienceWork> workExperiences;

    @OneToOne
    @JoinColumn(name = "CANDIDATE_ID")
    private Candidate candidate;

    public void setLanguages(Set<String> languages) {
        if (this.languages != null && languages != null) {
            this.languages.clear();
            this.languages.addAll(languages);
        } else {
            this.languages = languages;
        }
    }

    public void setIndustries(Set<String> industries) {
        if (this.industries != null && industries != null) {
            this.industries.clear();
            this.industries.addAll(industries);
        } else {
            this.industries = industries;
        }
    }

    public void setSpecializations(Set<String> specializations) {
        if (this.specializations != null && specializations != null) {
            this.specializations.clear();
            this.specializations.addAll(specializations);
        } else {
            this.specializations = specializations;
        }
    }

    public void setSkills(Set<String> skills) {
        if (this.skills != null && skills != null) {
            this.skills.clear();
            this.skills.addAll(skills);
        } else {
            this.skills = skills;
        }
    }

    public void setCustomers(Set<String> customers) {
        if (this.customers != null && customers != null) {
            this.customers.clear();
            this.customers.addAll(customers);
        } else {
            this.customers = customers;
        }
    }

    public void setWorkExperiences(Set<CandidateExperienceWork> workExperiences) {
        if (this.workExperiences != null && workExperiences != null) {
            this.workExperiences.clear();
            this.workExperiences.addAll(workExperiences);
        } else {
            this.workExperiences = workExperiences;
        }
    }
}
