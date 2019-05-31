package com.workable.matchmakers.adapter;

import com.google.common.collect.Maps;
import com.workable.matchmakers.adapter.dto.CvExtractDto;
import com.workable.matchmakers.adapter.dto.CvExtractResponseDto;
import com.workable.matchmakers.adapter.dto.CvFoundResponseDto;
import com.workable.matchmakers.dao.model.Candidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class CandidateAdapter extends Adapter {

    public String extractCV(Candidate candidate) {
        ResponseEntity<CvExtractResponseDto> correlation =  rest.postForEntity( atsUrl + "/matchmakers/sourcing_flow", toDto(candidate), CvExtractResponseDto.class);
        return correlation.getBody().getCorrelation_id();
    }

    public boolean found(Candidate candidate) {
        Map<String, String> params = Maps.newHashMap();
        params.put("email", candidate.getEmail());
        ResponseEntity<CvFoundResponseDto> response =  rest.getForEntity( atsUrl + "/matchmakers/candidates", CvFoundResponseDto.class, params);

        if (response.getBody() != null && candidate.getEmail().equalsIgnoreCase(response.getBody().getEmail())) {
            return true;
        } else {
            return false;
        }
    }

    private CvExtractDto toDto(Candidate candidate) {
        return CvExtractDto.builder()
                .candidate(CvExtractDto.CandidateDto.builder().fullname(candidate.getName()).email(candidate.getEmail()).resume_url(candidate.getCvUrl()).build())
                .build();
    }
}
