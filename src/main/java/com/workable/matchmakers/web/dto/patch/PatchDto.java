package com.workable.matchmakers.web.dto.patch;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

/**
 * https://tools.ietf.org/html/draft-ietf-appsawg-json-patch-10#section-4.1
 */
public class PatchDto {

	@NotNull
	@JsonProperty("operation")
	@ApiModelProperty(example = "replace", allowableValues = "add, replace, remove", required = true)
	private PatchOperation operation;

	@NotNull
	@ApiModelProperty(example = "name", required = true)
	private String field;

	@ApiModelProperty(example = "Panos Antonakos")
	private String value;

	public PatchOperation getOperation() {
		return operation;
	}

	public void setOperation(PatchOperation operation) {
		this.operation = operation;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PatchDto)) return false;

		PatchDto patchDto = (PatchDto) o;

		if (operation != patchDto.operation) return false;
		if (field != null ? !field.equals(patchDto.field) : patchDto.field != null) return false;
		return value != null ? value.equals(patchDto.value) : patchDto.value == null;
	}

	@Override
	public int hashCode() {
		int result = operation != null ? operation.hashCode() : 0;
		result = 31 * result + (field != null ? field.hashCode() : 0);
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
