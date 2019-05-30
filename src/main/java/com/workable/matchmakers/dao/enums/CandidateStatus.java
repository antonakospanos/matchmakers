package com.workable.matchmakers.dao.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum CandidateStatus {

    ACTIVE("Actively looking"),
    NORMAL("Looking around"),
    INACTIVE("Not interested");

    private static Map<String, CandidateStatus> map = new HashMap<>();

    static {
        for (CandidateStatus state : CandidateStatus.values()) {
            map.put(state.getDescription(), state);
        }
    }

    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    CandidateStatus(String description) {
        this.description = description;
    }

    public static CandidateStatus lookup(String description) {
        return map.get(description);
    }

    @JsonValue
    public String toValue() {
        return this.description;
    }

    @JsonCreator
    public static CandidateStatus forValue(String description) {
        for (CandidateStatus state : CandidateStatus.values()) {
            if (state.getDescription().equalsIgnoreCase(description)) {
                return state;
            }
        }
        return null;
    }
}
