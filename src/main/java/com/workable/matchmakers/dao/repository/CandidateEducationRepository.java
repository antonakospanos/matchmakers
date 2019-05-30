package com.workable.matchmakers.dao.repository;

import com.workable.matchmakers.dao.model.CandidateEducation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateEducationRepository extends JpaRepository<CandidateEducation, Long> {

    List<CandidateEducation> findByCandidate_Id(Long candidateId);
    List<CandidateEducation> findByCandidate_ExternalId(UUID candidateExternalId);

    List<CandidateEducation> findByCandidate_Id_AndDegree(Long candidateId, String degree);
    List<CandidateEducation> findByCandidate_ExternalId_AndDegree(UUID candidateExternalId, String degree);
}
