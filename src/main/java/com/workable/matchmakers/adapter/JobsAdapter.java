package com.workable.matchmakers.adapter;

import com.workable.matchmakers.adapter.dto.JobQueryDto;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.dao.model.CandidateEducation;
import com.workable.matchmakers.dao.model.CandidateExperienceWork;
import com.workable.matchmakers.web.dto.jobs.JobDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JobsAdapter extends AtsAdapter {

    public List<JobDto> list(Candidate candidate) {
        ResponseEntity<List> jobs =  rest.postForEntity( atsUrl + "/matchmakers/matching_jobs", toDto(candidate), List.class);

        return jobs.getBody();
    }

    private JobQueryDto toDto(Candidate candidate) {
        return JobQueryDto.builder()
                .title(candidate.getCandidateObjective().getRoles().stream().findFirst().orElse(null))
                .city(candidate.getCandidateObjective().getCity())
                .country(candidate.getCandidateObjective().getCountry())
                .candidate(JobQueryDto.CandidateDto.builder()
                        .name(candidate.getName())
                        .email(candidate.getEmail())
                        .education(candidate.getEducation().stream().map(CandidateEducation::getUniversity).collect(Collectors.toList()))
                        .experience(candidate.getExperience().getWorkExperiences().stream().map(CandidateExperienceWork::getRole).collect(Collectors.toList()))
                        .experience(new ArrayList<>(candidate.getExperience().getSkills()))
                        .build())
                .build();
    }
}
