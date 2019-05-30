package com.workable.matchmakers.web.dto.jobs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobDto {

    String logo;
    String company;
    String title;
    String jobId;
    String jobUrl;
    String description;
    Integer match;
    Publisher publisher;

    @Data
    @Builder
    static class Publisher {
        String name;
        String date;
    }
}
