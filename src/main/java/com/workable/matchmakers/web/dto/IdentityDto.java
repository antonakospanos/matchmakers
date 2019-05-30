package com.workable.matchmakers.web.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

/**
 * Identity DTO
 */
@JsonPropertyOrder({ "id"})
public class IdentityDto {

    @ApiModelProperty(example = "identity")
    private String id;

    public IdentityDto() {
    }

    public IdentityDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdentityDto)) return false;

        IdentityDto identityDto = (IdentityDto) o;

        return id != null ? id.equals(identityDto.id) : identityDto.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
