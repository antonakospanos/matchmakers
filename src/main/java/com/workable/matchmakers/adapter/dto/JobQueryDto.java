package com.workable.matchmakers.adapter.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JobQueryDto {

    String title;
    String city;
    String country;
    CandidateDto candidate;

    @Data
    @Builder
    public static class CandidateDto {
        String name;
        String email;
        List<String> education;
        List<String> experience;
        List<String> skills;
    }
}
