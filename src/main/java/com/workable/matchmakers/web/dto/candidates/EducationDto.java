package com.workable.matchmakers.web.dto.candidates;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.workable.matchmakers.dao.model.CandidateEducation;
import com.workable.matchmakers.web.dto.Dto;

import javax.validation.constraints.NotEmpty;

@JsonPropertyOrder({ "degree", "university", "start", "end" })
@ToString
@Getter
@Setter
public class EducationDto implements Dto<CandidateEducation> {

    @NotEmpty
    @ApiModelProperty(example = "MEng in Software Engineering", required = true)
    private String degree;

    @NotEmpty
    @ApiModelProperty(example = "National Technical University of Athens", required = true)
    private String university;

    @NotEmpty
    @ApiModelProperty(example = "2005", required = true)
    private Long start;

    @NotEmpty
    @ApiModelProperty(example = "2011", required = true)
    private Long end;

    @Override
    public EducationDto fromEntity(CandidateEducation entity) {
        setDegree(entity.getDegree());
        setUniversity(entity.getUniversity());
        setStart(entity.getYearStart());
        setEnd(entity.getYearEnd());

        return this;
    }

    @Override
    public CandidateEducation toEntity() {
        CandidateEducation education = new CandidateEducation();

        return toEntity(education);
    }

    @Override
    public CandidateEducation toEntity(CandidateEducation entity) {
        entity.setDegree(this.degree);
        entity.setUniversity(this.university);
        entity.setYearStart(this.start);
        entity.setYearEnd(this.end);

        return entity;
    }
}
