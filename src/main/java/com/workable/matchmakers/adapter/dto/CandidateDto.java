package com.workable.matchmakers.adapter.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CandidateDto {

    String fullname;
    String email;
    String resume_url;
}
