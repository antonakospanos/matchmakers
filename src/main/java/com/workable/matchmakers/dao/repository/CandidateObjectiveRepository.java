package com.workable.matchmakers.dao.repository;

import com.workable.matchmakers.dao.model.CandidateObjective;
import com.workable.matchmakers.dao.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CandidateObjectiveRepository extends JpaRepository<CandidateObjective, Long> {

    CandidateObjective findByCandidate_Id(Long candidateId);
    CandidateObjective findByCandidate_ExternalId(UUID candidateExternalId);

//    @Query("SELECT DISTINCT o FROM Candidate_Objective o WHERE :roles in o.roles")
    List<CandidateObjective> findByStatusAndRolesIn(@Param("status") String status, @Param("roles") Collection<String> roles);

    List<CandidateObjective> findDistinctByStatusAndRolesInAndCandidate_RegistrationStatus(String status, Collection<String> roles, RegistrationStatus registrationStatus);

}
