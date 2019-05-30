package com.workable.matchmakers.dao.converter;

import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.dao.repository.CandidateEducationRepository;
import com.workable.matchmakers.dao.repository.CandidateExperienceRepository;
import com.workable.matchmakers.dao.repository.CandidateObjectiveRepository;
import com.workable.matchmakers.dao.repository.CandidateRepository;
import com.workable.matchmakers.service.HashService;
import com.workable.matchmakers.web.dto.candidates.CandidateDto;
import com.workable.matchmakers.web.dto.patch.PatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CandidateConverter {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    CandidateEducationRepository candidateEducationRepository;

    @Autowired
    CandidateExperienceRepository candidateExperienceRepository;

    @Autowired
    CandidateObjectiveRepository candidateObjectiveRepository;

    @Autowired
    HashService hashService;

    /**
     * Password shall be set either upon registration or upon password reset using PATCH API
     *
     * @param candidateDto
     * @param candidate
     */
    public void setPassword(CandidateDto candidateDto, Candidate candidate) {
        // Store hashed password
        String hashedPassword = hashService.bCryptPassword(candidateDto.getPassword());
        candidate.setPassword(hashedPassword);
    }

    public void updateCandidate(PatchDto patchDto, Candidate candidate) {
        String field = patchDto.getField();
        String value = patchDto.getValue();

        if ("username".equals(field)) {
            candidate.setUsername(value);
        } else if ("password".equals(field)) {
            String hashedPassword = hashService.bCryptPassword(value);
            candidate.setPassword(hashedPassword);
            candidate.setExternalId(UUID.randomUUID()); // Update externalId used in Authentication Bearer too!
        } else if ("name".equals(field)) {
            candidate.setName(value);
        } else if ("email".equals(field)) {
            candidate.setEmail(value);
        } else if ("cellphone".equals(field)) {
            candidate.setCellphone(value);
        } else if ("experience".equals(field) || "education".equals(field)) {
            throw new UnsupportedOperationException("Please update the whole candidate resource, experience/education cannot be patched!");
        }
    }

    public Candidate cleanDependencies(Candidate candidate) {
        // education
        if (candidate.getEducation() != null) {
            candidate.getEducation().clear();
//            candidateEducationRepository.deleteAll(candidate.getEducation());
//            candidate.setEducation(null);
        }

        // experience
        if (candidate.getExperience() != null) {
            candidate.getExperience().getSkills().clear();
            candidate.getExperience().getWorkExperiences().clear();
            candidate.getExperience().getIndustries().clear();
            candidate.getExperience().getSpecializations().clear();
            candidate.getExperience().getLanguages().clear();
            candidate.getExperience().getCustomers().clear();
//            candidateExperienceRepository.delete(candidate.getExperience());
//            candidate.setExperience(null);
        }

        // objective
        if (candidate.getCandidateObjective() != null) {
            candidate.getCandidateObjective().getLocationsPrimary().clear();
            candidate.getCandidateObjective().getLocationsSecondary().clear();
            candidate.getCandidateObjective().getRoles().clear();
//            candidateObjectiveRepository.delete(candidate.getCandidateObjective());
//            candidate.setCandidateObjective(null);
        }

        return candidateRepository.save(candidate);
    }
}
