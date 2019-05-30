package com.workable.matchmakers.web.dto.interview;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@ToString
@Getter
@Setter
public class InterviewDto {

    @NotNull
    @ApiModelProperty(allowableValues = "eba3e4be-0d60-41ee-92e4-0c96b1af5b6e", required = true)
    private UUID candidateId;
}
