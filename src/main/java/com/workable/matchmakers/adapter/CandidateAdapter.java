package com.workable.matchmakers.adapter;

import com.workable.matchmakers.adapter.dto.CvExtractDto;
import com.workable.matchmakers.adapter.dto.CvExtractResponseDto;
import com.workable.matchmakers.dao.model.Candidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CandidateAdapter extends Adapter {

    public String extractCV(Candidate candidate) {
        ResponseEntity<CvExtractResponseDto> correlation =  rest.postForEntity( atsUrl + "/matchmakers/sourcing_flow", toDto(candidate), CvExtractResponseDto.class);
        return correlation.getBody().getCorrelation_id();
    }

    protected CvExtractDto toDto(Candidate candidate) {
        return CvExtractDto.builder()
                .candidate(CvExtractDto.CandidateDto.builder().fullname(candidate.getName()).email(candidate.getEmail()).resume_url(candidate.getCvUrl()).build())
                .build();
    }
}
