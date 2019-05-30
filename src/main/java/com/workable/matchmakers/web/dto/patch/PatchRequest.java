package com.workable.matchmakers.web.dto.patch;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import java.util.List;

public class PatchRequest {

	@ApiModelProperty(required = true)
	@Valid
	private List<PatchDto> patches;

	public List<PatchDto> getPatches() {
		return patches;
	}

	public void setPatches(List<PatchDto> patches) {
		this.patches = patches;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
