package com.workable.matchmakers.adapter;

import com.google.common.collect.Lists;
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
        ResponseEntity<Object> jobs =  rest.postForEntity( atsUrl + "/matchmakers/matching_jobs", toDto(candidate), Object.class);

        return (List<JobDto>) jobs.getBody();
    }

    private JobQueryDto toDto(Candidate candidate) {
        String title = "";//candidate.getCandidateObjective().getRoles().stream().findFirst().orElse(null);
        String city = "";//candidate.getCandidateObjective().getCity();
        String country = ""; //candidate.getCandidateObjective().getCountry();
        List<String> education = Lists.newArrayList(); // candidate.getEducation().stream().map(CandidateEducation::getUniversity).collect(Collectors.toList())
        List<String> experience = Lists.newArrayList(); // candidate.getExperience().getWorkExperiences().stream().map(CandidateExperienceWork::getRole).collect(Collectors.toList())
        List<String> skills = Lists.newArrayList(); // new ArrayList<>(candidate.getExperience().getSkills())

        return JobQueryDto.builder()
                .title(title)
                .city(city)
                .country(country)
                .candidate(JobQueryDto.CandidateDto.builder()
                        .name(candidate.getName())
                        .email(candidate.getEmail())
                        .education(education)
                        .experience(experience)
                        .experience(skills)
                        .build())
                .build();
    }
}
