package com.workable.matchmakers.adapter;

import com.google.common.collect.Lists;
import com.workable.matchmakers.adapter.dto.JobApplyDto;
import com.workable.matchmakers.adapter.dto.JobQueryDto;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.web.dto.jobs.JobDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class JobsAdapter extends Adapter {

    public List<JobDto> list(Candidate candidate) {
        ResponseEntity<Object> jobs =  rest.postForEntity( atsUrl + "/matchmakers/matching_jobs", toJobQueryDto(candidate), Object.class);

        return (List<JobDto>) jobs.getBody();
    }

    public void apply(Candidate candidate, String account, String jobId) {
        String baseUrl = atsUrl.replace("www", account);
        rest.postForLocation(baseUrl + "/jobs/" + jobId + "/candidates", toJobApplyDto(candidate));
    }

    private JobApplyDto toJobApplyDto(Candidate candidate) {
        return JobApplyDto.builder().firstname(candidate.getName()).lastname(candidate.getName()).email(candidate.getEmail()).build();
    }

    private JobQueryDto toJobQueryDto(Candidate candidate) {
        String title = "";
        if (candidate.getCandidateObjective() != null && candidate.getCandidateObjective().getRoles() != null) {
            title = candidate.getCandidateObjective().getRoles().stream().findFirst().orElse(null);
        }

        String city = "";
        if (candidate.getCandidateObjective() != null) {
            city= candidate.getCandidateObjective().getCity();
        }

        List<String> education = Lists.newArrayList(); // candidate.getEducation().stream().map(CandidateEducation::getUniversity).collect(Collectors.toList())
        List<String> experience = Lists.newArrayList(); // candidate.getExperience().getWorkExperiences().stream().map(CandidateExperienceWork::getRole).collect(Collectors.toList())
        List<String> skills = Lists.newArrayList(); // new ArrayList<>(candidate.getExperience().getSkills())

        return JobQueryDto.builder()
                .title(title)
                .city(city)
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
