package com.workable.matchmakers.web.dto.candidates;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.workable.matchmakers.dao.model.CandidateExperienceWork;
import com.workable.matchmakers.web.dto.Dto;

import javax.validation.constraints.NotEmpty;

@JsonPropertyOrder({ "role", "company", "start", "end" })
@ToString
@Getter
@Setter
public class ExperienceWorkDto implements Dto<CandidateExperienceWork> {

    @NotEmpty
    @ApiModelProperty(example = "Software Engineer", required = true)
    private String role;

    @NotEmpty
    @ApiModelProperty(example = "UBS", required = true)
    private String company;

    @NotEmpty
    @ApiModelProperty(example = "2015", required = true)
    private Long start;

    @ApiModelProperty(example = "2018")
    private Long end;

    @Override
    public ExperienceWorkDto fromEntity(CandidateExperienceWork entity) {
        setRole(entity.getRole());
        setCompany(entity.getCompany());
        setStart(entity.getYearStart());
        setEnd(entity.getYearEnd());

        return this;
    }

    @Override
    public CandidateExperienceWork toEntity() {
        CandidateExperienceWork experience = new CandidateExperienceWork();

        return toEntity(experience);
    }

    @Override
    public CandidateExperienceWork toEntity(CandidateExperienceWork entity) {
        entity.setRole(this.role);
        entity.setCompany(this.company);
        entity.setYearStart(this.start);
        entity.setYearEnd(this.end);

        return entity;
    }
}
