package com.workable.matchmakers.service;

import com.workable.matchmakers.util.Utils;
import javassist.NotFoundException;
import com.workable.matchmakers.dao.converter.CandidateConverter;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.dao.repository.CandidateRepository;
import com.workable.matchmakers.dao.enums.RegistrationStatus;
import com.workable.matchmakers.web.dto.candidates.CandidateBaseDto;
import com.workable.matchmakers.web.dto.candidates.CandidateDto;
import com.workable.matchmakers.web.dto.patch.PatchDto;
import com.workable.matchmakers.web.dto.response.CreateResponseData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateService {

	private final static Logger logger = LoggerFactory.getLogger(CandidateService.class);

	@Autowired
    CandidateRepository candidateRepository;

	@Autowired
	CandidateConverter candidateConverter;

	@Autowired
	HashService hashService;

	@Autowired
	MailService mailService;


	@Transactional
	public Candidate create(CandidateBaseDto candidateBaseDto) {
		CandidateDto candidateDto = new CandidateDto(candidateBaseDto);
		Candidate candidate = candidateRepository.findByUsername(candidateDto.getUsername());

		if (candidate != null) {
			throw new IllegalArgumentException("Candidate with username '" + candidate.getUsername() + "' already exists!");
		} else {
			// Add new Candidate in DB
			candidate = candidateDto.toEntity();
			candidateConverter.setPassword(candidateDto, candidate);

			candidateRepository.save(candidate);
			logger.info("New Candidate added: " + candidate);
		}

		return candidate;
	}

	// @Transactional
	public void replace(String username, CandidateBaseDto candidateBaseDto) {
		validateCandidate(username);

		// Update Candidate in DB
		CandidateDto candidateDto = new CandidateDto(candidateBaseDto);
		Candidate candidate = candidateRepository.findByUsername(username);
		replace(candidateDto, candidate);
	}


	// @Transactional
	public void replace(UUID externalId, CandidateDto candidateDto) {
		validateCandidate(externalId);

		// Update Candidate in DB
		Candidate candidate = candidateRepository.findByExternalId(externalId);
		replace(candidateDto, candidate);
	}

	// @Transactional
	public void replace(CandidateDto candidateDto, Candidate candidate) {
		validateNewUsername(candidate.getUsername(), candidateDto.getUsername());

		Candidate cleanedCandidate = candidateConverter.cleanDependencies(candidate);
 		Candidate updatedCandidate = candidateDto.toEntity(cleanedCandidate);

		candidateRepository.save(updatedCandidate);
	}

	@Transactional
	public CreateResponseData update(String username, List<PatchDto> patches) {
		validateCandidate(username);
		Candidate candidate = candidateRepository.findByUsername(username);

		CreateResponseData data = update(candidate, patches);
		logger.info("Candidate updated: " + candidate);

		return data;
	}

	@Transactional
	public CreateResponseData update(UUID externalId, List<PatchDto> patches) {
		validateCandidate(externalId);
		Candidate candidate = candidateRepository.findByExternalId(externalId);

		CreateResponseData data = update(candidate, patches);
		logger.info("Candidate updated: " + candidate);

		return data;
	}


	private CreateResponseData update(Candidate candidate, List<PatchDto> patches) {
		patches.forEach(patchDto -> {
			// Validate Candidate patches
			if (patchDto.getField().equals("username")) {
				validateNewUsername(candidate.getUsername(), patchDto.getValue());
			}
			// Update Candidate in DB
			candidateConverter.updateCandidate(patchDto, candidate);
		});

		candidateRepository.save(candidate);

		return new CreateResponseData(candidate.getExternalId().toString());
	}

	@Transactional
	public void delete(String username) {
		Candidate candidate = candidateRepository.findByUsername(username);
		if (candidate == null) {
			throw new IllegalArgumentException("Candidate '" + username + "' does not exist!");
		} else {
			candidateRepository.delete(candidate);
		}

		logger.warn("Candidate deleted: " + candidate);
	}

	@Transactional
	public void delete(UUID externalId) {
		Candidate candidate = candidateRepository.findByExternalId(externalId);
		if (candidate == null) {
			throw new IllegalArgumentException("Candidate '" + externalId + "' does not exist!");
		} else {
			candidateRepository.delete(candidate);
		}

		logger.warn("Candidate deleted: " + candidate);
	}

	public void resetPassword(Candidate candidate) {
		String newPassword = Utils.generatePassword();
		String hashedPassword = hashService.bCryptPassword(newPassword);
		candidate.setPassword(hashedPassword);
		candidate.setExternalId(UUID.randomUUID()); // Update externalId used in Authentication Bearer too!

		candidateRepository.save(candidate);
		logger.info("Password reset on candidate: " + candidate);

		mailService.sendPasswordReset(candidate, newPassword);
	}

	public CandidateDto find(String username, String password) throws NotFoundException {
		List<CandidateDto> candidates = list(username);

		if (candidates == null || candidates.isEmpty()) {
			throw new NotFoundException("No user found with username '" + username + "'");
		} else if (candidates.size() > 1) {
			throw new RuntimeException("Multiple candidates found with username '" + username + "'");
		} else {
			CandidateDto candidateDto = candidates.get(0);
			if (!hashService.matches(password, candidateDto.getPassword())) {
				throw new AuthorizationServiceException("Invalid password '" + password + "'");
			} else {
				return candidateDto;
			}
		}
	}

	public List<CandidateDto> list(String username) {
		List<CandidateDto> candidateDtos;

		if (StringUtils.isNotBlank(username)) {
			Candidate candidate = candidateRepository.findByUsername(username);
			candidateDtos = list(candidate);
		} else {
			candidateDtos = listAll();
		}

		return candidateDtos;
	}

	public List<CandidateDto> list(UUID externalId) {
		List<CandidateDto> candidateDtos;

		if (externalId != null) {
			Candidate candidate = candidateRepository.findByExternalId(externalId);
			candidateDtos = list(candidate);
		} else {
			candidateDtos = listAll();
		}

		return candidateDtos;
	}

	public boolean exists(UUID externalId) {
		boolean exists = false;

		if (externalId != null) {
			Candidate candidate = candidateRepository.findByExternalId(externalId);
			exists = candidate != null;
		}

		return exists;
	}

	public Candidate find(UUID externalId) {
		Candidate candidate = null;

		if (externalId != null) {
			candidate = candidateRepository.findByExternalId(externalId);
		}

		return candidate;
	}

	public List<CandidateDto> list(Candidate candidate) {
		List<CandidateDto> candidateDtos = new ArrayList<>();

		if (candidate != null) {
			CandidateDto candidateDto = new CandidateDto().fromEntity(candidate);
			candidateDtos.add(candidateDto);
		}

		return candidateDtos;
	}

	public List<CandidateDto> listAll() {
		List<Candidate> candidates = candidateRepository.findAll();

		return candidates.stream()
				.map(candidate -> new CandidateDto().fromEntity(candidate))
				.collect(Collectors.toList());
	}

	// ************************ Validations ************************ //
	public void validateCandidate(String username) {
		if (StringUtils.isNotBlank(username)) {
			Candidate candidate = candidateRepository.findByUsername(username);
			if (candidate == null) {
				throw new IllegalArgumentException("Candidate with username '" + username + "' does not exist!");
			}
		}
	}

	public void validateCandidate(UUID externalId) {
		if (externalId != null) {
			Candidate candidate = candidateRepository.findByExternalId(externalId);
			if (candidate == null) {
				throw new IllegalArgumentException("Candidate with id '" + externalId + "' does not exist!");
			}
		}
	}

	public void validateNewUsername(String oldUsername, String newUsername) {
		if (StringUtils.isNotBlank(newUsername)) {
			// Check that resource does not conflict
			Candidate candidate = candidateRepository.findByUsername(newUsername);
			if (!oldUsername.equals(newUsername) && candidate != null) {
				// Illegal username replacement
				throw new IllegalArgumentException("Candidate with username '" + newUsername + "' already exists!");
			}
		}
	}

	public void updateRegistration(UUID externalId, RegistrationStatus registrationStatus) {
		validateCandidate(externalId);

		// Update Candidate in DB
		Candidate candidate = candidateRepository.findByExternalId(externalId);
		candidate.setRegistrationStatus(registrationStatus);
		candidateRepository.save(candidate);

		if (RegistrationStatus.VALIDATED == registrationStatus) {
			mailService.sendCandidateRegistrationIsValidated(candidate);
		} else if (RegistrationStatus.COMPLETED == registrationStatus) {
			mailService.sendCandidateRegistrationIsCompleted(candidate);
			// mailService.sendCandidateWelcome(candidate);
		}
	}
}
