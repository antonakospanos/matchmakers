package com.workable.matchmakers.web.dto.candidates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.dao.model.CandidateEducation;
import com.workable.matchmakers.dao.model.CandidateExperience;
import com.workable.matchmakers.dao.model.CandidateObjective;
import com.workable.matchmakers.web.dto.Dto;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AccountDto
 */
@JsonPropertyOrder({ "id", "password", "name", "email", "cellphone", "linkedInUrl", "facebookUrl", "cvUrl", "education", "experience", "objective" })
@ToString
@Getter
@Setter
public class CandidateDto extends CandidateBaseDto implements Dto<Candidate> {

	public static List<String> fields = Arrays.asList(CandidateBaseDto.class.getDeclaredFields())
			.stream()
			.map(field -> field.getName())
			.collect(Collectors.toList());

	@NotNull
	@ApiModelProperty(example = "eba3e4be-0d60-41ee-92e4-0c96b1af5b6e", required = true)
	private UUID id;


	public CandidateDto() {
	}

	public CandidateDto(UUID id, String password, String name, String email,
						String cellphone, String linkedInUrl, String facebookUrl, String cvUrl,
						List<EducationDto> education, ExperienceDto experience, ObjectiveDto objective) {
		this.id = id;
		setPassword(password);
		setName(name);
		setEmail(email);
		setCellphone(cellphone);
		setLinkedInUrl(linkedInUrl);
		setFacebookUrl(facebookUrl);
		setCvUrl(cvUrl);
		setEducation(education);
		setExperience(experience);
		setObjective(objective);
	}

	public CandidateDto(UUID id, CandidateBaseDto candidateBaseDto) {
		this(candidateBaseDto);
		this.id = id;
	}

	public CandidateDto(CandidateBaseDto candidateBaseDto) {
		super(candidateBaseDto.getPassword(), candidateBaseDto.getName(),
				candidateBaseDto.getEmail(), candidateBaseDto.getCellphone(),
				candidateBaseDto.getLinkedInUrl(), candidateBaseDto.getFacebookUrl(), candidateBaseDto.getCvUrl(),
				candidateBaseDto.getEducation(), candidateBaseDto.getExperience(), candidateBaseDto.getObjective());
	}

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@JsonIgnore
	@ApiModelProperty(hidden = true)
	@Override
	public void setPassword(String password) {
		super.setPassword(password);
	}

	@Override
	public CandidateDto fromEntity(Candidate candidate) {
		this.id = candidate.getExternalId();

		setPassword(candidate.getPassword());
		setEmail(candidate.getEmail());
		setName(candidate.getName());
		setCellphone(candidate.getCellphone());
		setLinkedInUrl(candidate.getLinkedInUrl());
		setFacebookUrl(candidate.getFacebookUrl());
		setCvUrl(candidate.getCvUrl());

		if (candidate.getEducation() != null) {
			List<EducationDto> educations =
					candidate.getEducation()
							.stream()
							.map(education -> new EducationDto().fromEntity(education))
							.collect(Collectors.toList());
			setEducation(educations);
		}

		if (candidate.getExperience() != null) {
			setExperience(new ExperienceDto().fromEntity(candidate.getExperience()));
		}

		if (candidate.getCandidateObjective() != null) {
			setObjective(new ObjectiveDto().fromEntity(candidate.getCandidateObjective()));
		}

		return this;
	}

	@Override
	public Candidate toEntity() {
		Candidate candidate = new Candidate();

		return toEntity(candidate);
	}

	@Override
	public Candidate toEntity(Candidate candidate) {
		// Do not pass this.getId(), CANDIDATE_EXTERNAL_ID is auto-generated!
		candidate.setName(this.getName());
		candidate.setEmail(this.getEmail());
		candidate.setCellphone(this.getCellphone());
		candidate.setLinkedInUrl(this.getLinkedInUrl());
		candidate.setFacebookUrl(this.getFacebookUrl());
		candidate.setCvUrl(this.getCvUrl());

		if (this.getEducation() != null) {
			Set<CandidateEducation> educations =
					this.getEducation()
					.stream()
					.map(educationDto -> {
						CandidateEducation education = educationDto.toEntity();
						education.setCandidate(candidate);
						return education;
					})
					.collect(Collectors.toSet());
			candidate.setEducation(educations);
		}

		if (this.getExperience() != null) {
			CandidateExperience experience = this.getExperience().toEntity();
			experience.setCandidate(candidate);
			candidate.setExperience(experience);
		}

		if (this.getObjective() != null) {
			CandidateObjective objective = this.getObjective().toEntity();
			objective.setCandidate(candidate);
			candidate.setCandidateObjective(objective);
		}

		// DAO: Add hash password

		return candidate;
	}
}
