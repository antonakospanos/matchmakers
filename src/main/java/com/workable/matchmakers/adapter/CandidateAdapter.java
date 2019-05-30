package com.workable.matchmakers.adapter;

import com.workable.matchmakers.adapter.dto.CvUploadDto;
import com.workable.matchmakers.adapter.dto.CvUploadResponseDto;
import com.workable.matchmakers.dao.model.Candidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CandidateAdapter extends AtsAdapter {

    public String upload(Candidate candidate) {
        ResponseEntity<CvUploadResponseDto> correlation =  rest.postForEntity( atsUrl + "/matchmakers/sourcing_flow", toDto(candidate), CvUploadResponseDto.class);
        return correlation.getBody().getCorrelation_id();
    }

    protected CvUploadDto toDto(Candidate candidate) {
        return CvUploadDto.builder()
                .fullname(candidate.getName())
                .email(candidate.getEmail())
                .resume_url(candidate.getCvUrl())
                .build();
    }
}
