package com.workable.matchmakers.web.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Identity DTO
 */
@JsonPropertyOrder({ "id", "name"})
@Getter
@Setter
public class IdentityDto extends IdentityBaseDto {

    @ApiModelProperty(example = "Panos Antonakos")
    String name;

    public IdentityDto() {
    }

    public IdentityDto(String id, String name) {
        super(id);
        this.name = name;
    }
}
