package com.workable.matchmakers.dao.model;

import lombok.Getter;
import lombok.Setter;
import com.workable.matchmakers.dao.enums.RegistrationStatus;
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
@Table(name = "CANDIDATE")
@Getter
@Setter
public class Candidate extends User {

	private String cellphone;

	private String linkedInUrl;

	private String facebookUrl;

	@OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL , fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<CandidateEducation> education;

	@OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private CandidateExperience experience;

	@OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	private CandidateObjective candidateObjective;

	@Enumerated(value = EnumType.STRING)
	private RegistrationStatus registrationStatus = RegistrationStatus.PENDING;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name="CV_ID")
	private Blob cv;

	public void setEducation(Set<CandidateEducation> education) {
		if (this.education != null && education != null) {
			this.education.clear();
			this.education.addAll(education);
		} else {
			this.education = education;
		}
	}
}
