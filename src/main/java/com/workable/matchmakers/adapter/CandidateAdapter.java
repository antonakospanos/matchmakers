package com.workable.matchmakers.adapter;

import com.workable.matchmakers.adapter.dto.CandidateDto;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.web.dto.jobs.JobDto;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class CandidateAdapter extends AtsAdapter {

    public void upload(Candidate candidate) {
        rest.postForEntity( atsUrl + "/matchmakers/sourcing_flow", toDto(candidate), Void.class);
    }

    protected CandidateDto toDto(Candidate candidate) {
        return CandidateDto.builder()
                .fullname(candidate.getName())
                .email(candidate.getEmail())
                .resume_url(candidate.getCvUrl())
                .build();
    }
}
