package com.workable.matchmakers.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.workable.matchmakers.dao.enums.RegistrationStatus;

import javax.validation.constraints.NotNull;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationStatusDto {

    @NotNull
    @ApiModelProperty(dataType = "string", example = "VALIDATED", allowableValues = "PENDING, VALIDATED, COMPLETED",
            value = "The status of the registration in the platform", required = true)
    RegistrationStatus status;

    public static RegistrationStatusDto of(RegistrationStatus status) {
        return new RegistrationStatusDto(status);
    }
}
