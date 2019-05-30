package com.workable.matchmakers.web.dto.patch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PatchOperation {

	ADD, REPLACE, REMOVE;

	@Override
	public String toString() {
		return this.name().toLowerCase();
	}

	@JsonValue
	public String toValue() {
		return this.toString();
	}

	@JsonCreator
	public static PatchOperation forValue(String value) {
		for (PatchOperation operation : PatchOperation.values()) {
			if (operation.toString().equals(value)) {
				return operation;
			}
		}
		return null;
	}
}
