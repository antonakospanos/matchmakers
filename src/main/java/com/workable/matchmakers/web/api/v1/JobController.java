package com.workable.matchmakers.web.api.v1;

import com.google.common.collect.Lists;
import com.workable.matchmakers.dao.model.Blob;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.dao.repository.CandidateRepository;
import com.workable.matchmakers.service.CandidateService;
import com.workable.matchmakers.service.JobService;
import com.workable.matchmakers.service.MailService;
import com.workable.matchmakers.web.api.MatchmakersBaseController;
import com.workable.matchmakers.web.dto.IdentityDto;
import com.workable.matchmakers.web.dto.RegistrationStatusDto;
import com.workable.matchmakers.web.dto.candidates.CandidateBaseDto;
import com.workable.matchmakers.web.dto.candidates.CandidateDto;
import com.workable.matchmakers.web.dto.jobs.JobDto;
import com.workable.matchmakers.web.dto.patch.PatchRequest;
import com.workable.matchmakers.web.dto.response.CreateResponse;
import com.workable.matchmakers.web.dto.response.CreateResponseData;
import com.workable.matchmakers.web.dto.response.Response;
import com.workable.matchmakers.web.dto.response.ResponseBase;
import com.workable.matchmakers.web.enums.Result;
import com.workable.matchmakers.web.support.ControllerUtils;
import com.workable.matchmakers.web.support.SecurityHelper;
import com.workable.matchmakers.web.validator.BlobValidator;
import com.workable.matchmakers.web.validator.PatchValidator;
import io.swagger.annotations.*;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.workable.matchmakers.web.api.MatchmakersBaseController.API_BASE;

@RestController
@Api(value = "Job API", tags = "Jobs", position = 2, description = "Job Management")
@RequestMapping(value = API_BASE + "/jobs")
@Slf4j
public class JobController extends MatchmakersBaseController {

	@Autowired
	JobService jobService;

	@Autowired
	CandidateService candidateService;

	@ApiOperation(value = "Lists all matching jobs", response = JobDto.class, responseContainer="List")
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The candidate's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			defaultValue = "Bearer admin",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<Iterable> list(@RequestHeader(value = "Authorization") String authorization) {
		UUID accessToken = SecurityHelper.getUuidToken(authorization);
		candidateService.validateCandidate(accessToken);

		List<JobDto> jobs = Lists.newArrayList();
		try {
			Candidate candidate = candidateService.find(accessToken);
			jobs = jobService.list(candidate);
		} catch (Exception e) {
			log.error("ERROR: ", e);
		}

		return ResponseEntity.status(HttpStatus.OK).body(jobs);
	}

	@ApiOperation(value = "Applies to the requested job", response = ResponseBase.class)
	@RequestMapping(value = "/{id}", produces = {"application/json"},	method = RequestMethod.POST)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The candidate's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			defaultValue = "Bearer admin",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<ResponseBase> apply(@RequestHeader(value = "Authorization") String authorization, @PathVariable String id, @RequestParam String account) {
		UUID accessToken = SecurityHelper.getUuidToken(authorization);
		candidateService.validateCandidate(accessToken);

		try {
			Candidate candidate = candidateService.find(accessToken);
			jobService.apply(candidate, account, id);
		} catch (Exception e) {
			log.error("ERROR: ", e);
		}

		return ResponseEntity.ok().body(ResponseBase.Builder().build(Result.SUCCESS));
	}
}
