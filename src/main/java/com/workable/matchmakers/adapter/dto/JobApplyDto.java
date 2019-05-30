package com.workable.matchmakers.adapter.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JobApplyDto {

    String firstname;
    String lastname;
    String email;
}

