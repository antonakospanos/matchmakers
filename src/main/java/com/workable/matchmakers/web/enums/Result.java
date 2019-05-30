package com.workable.matchmakers.web.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Result {

	SUCCESS("The request was handled successfully"),
	BAD_REQUEST("The request is invalid"),
	DUPLICATE_REQUEST("The request is duplicate"),
	TOO_MANY_REQUESTS("Rate limit is violated"),
	UNAUTHORIZED("The user is not authorized"),
	NOT_FOUND("The resource is not found"),
	GENERIC_ERROR("Internal Server Error");

	private String description;

	Result(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	@JsonValue
	public String toString() {
		return this.name();
	}

	@JsonCreator
	public static Result forValue(String value) {
		for (Result result : Result.values()) {
			if (String.valueOf(result.name()).equals(value)) {
				return result;
			}
		}
		return null;
	}

}
