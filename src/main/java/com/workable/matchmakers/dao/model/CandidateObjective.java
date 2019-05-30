package com.workable.matchmakers.dao.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Cacheable
@DynamicUpdate
@DynamicInsert
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "matchmakers.entity-cache")
@Table(name = "CANDIDATE_OBJECTIVE")
@Getter
@Setter
public class CandidateObjective extends ResourceBase {

    @ElementCollection(targetClass=String.class)
    private Set<String> roles;

    @ElementCollection
    private Set<String> locationsPrimary;

    @ElementCollection
    private Set<String> locationsSecondary;

    private String status;

    private Long salaryFrom;

    private Long salaryTo;

    private LocalDate availabilityInterview;

    private LocalDate availabilityWork;

    @OneToOne
    @JoinColumn(name = "CANDIDATE_ID")
    private Candidate candidate;

    public void setRoles(Set<String> roles) {
        if (this.roles != null && roles != null) {
            this.roles.clear();
            this.roles.addAll(roles);
        } else {
            this.roles = roles;
        }
    }

    public void setLocationsPrimary(Set<String> locationsPrimary) {
        if (this.locationsPrimary != null && locationsPrimary != null) {
            this.locationsPrimary.clear();
            this.locationsPrimary.addAll(locationsPrimary);
        } else {
            this.locationsPrimary = locationsPrimary;
        }
    }

    public void setLocationsSecondary(Set<String> locationsSecondary) {
        if (this.locationsSecondary != null && locationsSecondary != null) {
            this.locationsSecondary.clear();
            this.locationsSecondary.addAll(locationsSecondary);
        } else {
            this.locationsSecondary = locationsSecondary;
        }
    }
}
