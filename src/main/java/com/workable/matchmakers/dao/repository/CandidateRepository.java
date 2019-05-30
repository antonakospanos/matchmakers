package com.workable.matchmakers.dao.repository;

import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.dao.model.QCandidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

	QCandidate CANDIDATE = QCandidate.candidate;

	Candidate findByExternalId(UUID externalId);

	Candidate findByUsername(String username);

	List<Candidate> findByCandidateObjective_Roles(String role);

}
