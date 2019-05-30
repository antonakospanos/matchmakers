package com.workable.matchmakers.web.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * IdentityBase DTO
 */
@JsonPropertyOrder({ "id"})
@Getter
@Setter
public class IdentityBaseDto {

    @ApiModelProperty(example = "eba3e4be-0d60-41ee-92e4-0c96b1af5b6e")
    private String id;

    public IdentityBaseDto() {
    }

    public IdentityBaseDto(String id) {
        this.id = id;
    }

}
