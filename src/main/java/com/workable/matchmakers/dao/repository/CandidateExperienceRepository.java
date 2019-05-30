package com.workable.matchmakers.dao.repository;

import com.workable.matchmakers.dao.model.CandidateExperience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidateExperienceRepository extends JpaRepository<CandidateExperience, Long> {

    CandidateExperience findByCandidate_Id(Long candidateId);
    CandidateExperience findByCandidate_ExternalId(UUID candidateExternalId);
}
