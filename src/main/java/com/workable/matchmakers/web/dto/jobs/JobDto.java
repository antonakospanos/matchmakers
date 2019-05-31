package com.workable.matchmakers.web.dto.jobs;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JobDto {

    String logo;
    String company;
    String company_subdomain;
    String title;
    String jobId;
    String jobUrl;
    String description;
    Integer match;
    Publisher publisher;
    List<String> skills;

    @Data
    @Builder
    static class Publisher {
        String name;
        String date;
    }
}
