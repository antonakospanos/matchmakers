package com.workable.matchmakers.web.dto.candidates;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.workable.matchmakers.dao.model.CandidateObjective;
import com.workable.matchmakers.dao.enums.CandidateStatus;
import com.workable.matchmakers.web.dto.Dto;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@JsonPropertyOrder({ "roles", "locationsPrimary", "locationsSecondary", "status", "salaryFrom", "salaryTo", "availabilityInterview", "availabilityWork" })
@ToString
@Getter
@Setter
public class ObjectiveDto implements Dto<CandidateObjective> {

    @NotEmpty
    @ApiModelProperty(allowableValues = "Senior Software Engineer", required = true)
    private Set<String> roles;

    @NotNull
    @ApiModelProperty(allowableValues = "London", required = true)
    private Set<String> locationsPrimary;

    @ApiModelProperty(allowableValues = "Athens")
    private Set<String> locationsSecondary;

    @NotEmpty
    @ApiModelProperty(example = "Actively looking", required = true)
    private CandidateStatus status;

    @NotEmpty
    @ApiModelProperty(example = "20000", required = true)
    private Long salaryFrom;

    @NotEmpty
    @ApiModelProperty(example = "25000", required = true)
    private Long salaryTo;

    @NotEmpty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty(example = "2018-06-01", required = true)
    private LocalDate availabilityInterview;

    @NotEmpty
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty(example = "2018-07-01", required = true)
    private LocalDate availabilityWork;

    @Override
    public ObjectiveDto fromEntity(CandidateObjective entity) {
        setRoles(entity.getRoles());
        setLocationsPrimary(entity.getLocationsPrimary());
        setLocationsSecondary(entity.getLocationsSecondary());
        setStatus(CandidateStatus.forValue(entity.getStatus()));
        setSalaryFrom(entity.getSalaryFrom());
        setSalaryTo(entity.getSalaryTo());
        setAvailabilityInterview(entity.getAvailabilityInterview());
        setAvailabilityWork(entity.getAvailabilityWork());

        return this;
    }

    @Override
    public CandidateObjective toEntity() {
        CandidateObjective objective = new CandidateObjective();

        return toEntity(objective);
    }

    @Override
    public CandidateObjective toEntity(CandidateObjective entity) {
        entity.setRoles(this.getRoles());
        entity.setLocationsPrimary(this.getLocationsPrimary());
        entity.setLocationsSecondary(this.getLocationsSecondary());
        entity.setStatus(this.getStatus().getDescription());
        entity.setSalaryFrom(this.getSalaryFrom());
        entity.setSalaryTo(this.getSalaryTo());
        entity.setAvailabilityInterview(this.getAvailabilityInterview());
        entity.setAvailabilityWork(this.getAvailabilityWork());

        return entity;
    }
}
