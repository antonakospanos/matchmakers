package com.workable.matchmakers.web.dto.candidates;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.workable.matchmakers.dao.model.CandidateExperience;
import com.workable.matchmakers.dao.model.CandidateExperienceWork;
import com.workable.matchmakers.web.dto.Dto;

import javax.validation.constraints.NotEmpty;
import java.util.Set;
import java.util.stream.Collectors;

@JsonPropertyOrder({"years", "workExperiences", "specializations", "skills", "industries", "seniorManagement",
        "degreeLevel", "degreeCategory", "extraCurriculumActivities", "languages", "customers"})
@ToString
@Getter
@Setter
public class ExperienceDto implements Dto<CandidateExperience> {

    @NotEmpty
    @ApiModelProperty(example = "5", required = true)
    private Long years;

    private Set<ExperienceWorkDto> workExperiences;

    @ApiModelProperty(allowableValues = "Java Development")
    private Set<String> specializations;

    @ApiModelProperty(allowableValues = "Docker")
    private Set<String> skills;

    @ApiModelProperty(allowableValues = "Mobile Commerce")
    private Set<String> industries;

    @ApiModelProperty(example = "false")
    private Boolean seniorManagement;

    @ApiModelProperty(allowableValues = "MA / MSc")
    private String degreeLevel;

    @ApiModelProperty(allowableValues = "Engineering")
    private String degreeCategory;

    @ApiModelProperty(example = "true")
    private Boolean extraCurriculumActivities;

    @ApiModelProperty(allowableValues = "English")
    private Set<String> languages;

    @ApiModelProperty(allowableValues = "LATAM")
    private Set<String> customers;

    @Override
    public ExperienceDto fromEntity(CandidateExperience entity) {
        setYears(entity.getYears());
        setSeniorManagement(entity.getSeniorManagement());
        setIndustries(entity.getIndustries());
        setSpecializations(entity.getSpecializations());
        setSkills(entity.getSkills());
        setDegreeCategory(entity.getDegreeCategory());
        setDegreeLevel(entity.getDegreeLevel());
        setExtraCurriculumActivities(entity.getExtraCurriculumActivities());
        setLanguages(entity.getLanguages());
        setCustomers(entity.getCustomers());

        if (entity.getWorkExperiences() != null) {
            Set<ExperienceWorkDto> experiences =
                    entity.getWorkExperiences()
                            .stream()
                            .map(workExperience -> new ExperienceWorkDto().fromEntity(workExperience))
                            .collect(Collectors.toSet());

            setWorkExperiences(experiences);
        }

        return this;
    }

    @Override
    public CandidateExperience toEntity() {
        CandidateExperience experience = new CandidateExperience();

        return toEntity(experience);
    }

    @Override
    public CandidateExperience toEntity(CandidateExperience entity) {
        entity.setYears(this.years);
        entity.setSeniorManagement(this.seniorManagement);
        entity.setIndustries(this.industries);
        entity.setSpecializations(this.specializations);
        entity.setSkills(this.skills);
        entity.setDegreeCategory(this.degreeCategory);
        entity.setDegreeLevel(this.degreeLevel);
        entity.setExtraCurriculumActivities(this.extraCurriculumActivities);
        entity.setLanguages(this.languages);
        entity.setCustomers(this.customers);

        if (this.getWorkExperiences() != null) {
            Set<CandidateExperienceWork> workExperiences =
                    this.getWorkExperiences()
                            .stream()
                            .map(workExperienceDto -> {
                                CandidateExperienceWork workExperience = workExperienceDto.toEntity();
                                workExperience.setCandidateExperience(entity);

                                return workExperience;
                            })
                            .collect(Collectors.toSet());

            entity.setWorkExperiences(workExperiences);
        }

        return entity;
    }
}
