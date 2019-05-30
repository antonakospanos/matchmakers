package com.workable.matchmakers.web.api.v1;

import io.swagger.annotations.*;
import javassist.NotFoundException;
import com.workable.matchmakers.dao.model.Blob;
import com.workable.matchmakers.dao.model.Candidate;
import com.workable.matchmakers.dao.repository.CandidateRepository;
import com.workable.matchmakers.service.CandidateService;
import com.workable.matchmakers.service.MailService;
import com.workable.matchmakers.web.api.MatchmakersBaseController;
import com.workable.matchmakers.web.dto.IdentityDto;
import com.workable.matchmakers.web.dto.RegistrationStatusDto;
import com.workable.matchmakers.web.dto.candidates.CandidateBaseDto;
import com.workable.matchmakers.web.dto.candidates.CandidateDto;
import com.workable.matchmakers.web.dto.patch.PatchRequest;
import com.workable.matchmakers.web.dto.response.CreateResponse;
import com.workable.matchmakers.web.dto.response.CreateResponseData;
import com.workable.matchmakers.web.dto.response.Response;
import com.workable.matchmakers.web.dto.response.ResponseBase;
import com.workable.matchmakers.web.enums.Result;
import com.workable.matchmakers.web.support.ControllerUtils;
import com.workable.matchmakers.web.validator.BlobValidator;
import com.workable.matchmakers.web.validator.PatchValidator;
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
@Api(value = "Candidate API", tags = "Candidates", position = 2, description = "Candidate Management")
@RequestMapping(value = API_BASE + "/candidates")
public class CandidateController extends MatchmakersBaseController {

	@Autowired
	CandidateService service;

	@Autowired
	MailService mailService;

	@Autowired
	CandidateRepository candidateRepository;

	@Autowired
	BlobValidator validator;

	@RequestMapping(value = "", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.POST)
	@ApiOperation(value = "Creates the candidate", response = CreateResponse.class)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The candidate is created!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<CreateResponse> create(UriComponentsBuilder uriBuilder, @Valid @RequestBody CandidateBaseDto candidateBaseDto) {
		ResponseEntity<CreateResponse> response;

		Candidate candidate = service.create(candidateBaseDto);

		mailService.sendCandidateRegistrationIsPending(candidate);

		CreateResponseData data = new CreateResponseData(candidate.getExternalId().toString());
		UriComponents uriComponents =	uriBuilder.path(API_BASE + "/candidates/{id}").buildAndExpand(data.getId());
		CreateResponse responseBase = CreateResponse.Builder().build(Result.SUCCESS).data(data);
		response = ResponseEntity.created(uriComponents.toUri()).body(responseBase);

		return response;
	}

	@RequestMapping(value = "/{id}", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.PUT)
	@ApiOperation(value = "Replaces all candidate's information", response = ResponseBase.class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The candidate's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			defaultValue = "Bearer admin",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The candidate is replaced!", response = ResponseBase.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<ResponseBase> replace(@PathVariable UUID id, @Valid @RequestBody CandidateDto candidateDto) {
		ResponseEntity<ResponseBase> response;

		service.replace(id, candidateDto);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.ok().body(responseBase);

		return response;
	}

	@RequestMapping(value = "/{id}", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.PATCH)
	@ApiOperation(value = "Updates the candidate's information", response = CreateResponse.class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The candidate's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			defaultValue = "Bearer admin",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The candidate is updated!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<CreateResponse> update(@PathVariable UUID id, @Valid @RequestBody PatchRequest request) {
		ResponseEntity<CreateResponse> response;

		PatchValidator.validatePatchRequest(request, CandidateDto.fields);

		CreateResponseData data = service.update(id, request.getPatches());
		CreateResponse responseBase = CreateResponse.Builder().build(Result.SUCCESS).data(data);
		response = ResponseEntity.ok().body(responseBase);

		return response;
	}

	@ApiOperation(value = "Deletes the candidate", response = ResponseBase.class)
	@RequestMapping(value = "/{id}", produces = {"application/json"},	method = RequestMethod.DELETE)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The candidate's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			defaultValue = "Bearer admin",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseBase> delete(@PathVariable UUID id) {
		ResponseEntity<ResponseBase> response;

		service.delete(id);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);
		response = ResponseEntity.status(HttpStatus.OK).body(responseBase);

		return response;
	}

	@ApiOperation(value = "Authenticates the candidate returning his identification", response = IdentityDto.class)
	@RequestMapping(value = "/id", produces = {"application/json"},	method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The user is authenticated", response = IdentityDto.class),
			@ApiResponse(code = 401, message = "The credentials are invalid"),
			@ApiResponse(code = 404, message = "No user found!"),
			@ApiResponse(code = 500, message = "Server Error")})
	public ResponseEntity<IdentityDto> listAccount(UriComponentsBuilder uriBuilder, @RequestParam String username, @RequestParam String password) throws NotFoundException {
		ResponseEntity<IdentityDto> response;
		CandidateDto account = service.find(username, password);

		if (account == null) {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			IdentityDto identityDto = new IdentityDto(account.getId().toString(), account.getName());
			UriComponents uriComponents = uriBuilder.path(API_BASE + "/candidates/{id}").buildAndExpand(identityDto.getId());
			response = ResponseEntity.status(HttpStatus.OK).location(uriComponents.toUri()).body(identityDto);
		}

		return response;
	}

	@ApiOperation(value = "Lists all candidates", response = CandidateDto.class, responseContainer="List")
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
	public ResponseEntity<Iterable> listAllAccounts() {
		List<CandidateDto> candidates = service.listAll();

		return ControllerUtils.listResources(candidates);
	}

	@ApiOperation(value = "Lists the candidate", response = CandidateDto.class)
	@RequestMapping(value = "/{id}", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The candidate's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			defaultValue = "Bearer admin",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<CandidateDto> listAccount(@PathVariable UUID id) {
		List<CandidateDto> candidates = service.list(id);

		return ControllerUtils.listResource(candidates);
	}

	@ApiOperation(value = "Resets the candidate's password", response = ResponseBase.class)
	@RequestMapping(value = "/password", produces = {"application/json"},	method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseBase> resetPassword(@RequestParam String username) {
		service.validateCandidate(username);

        Candidate candidate = candidateRepository.findByUsername(username);
		service.resetPassword(candidate);

		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);

		return ResponseEntity.status(HttpStatus.OK).body(responseBase);
	}

	@ApiOperation(value = "Lists the candidate's registration status", response = Response.class)
	@RequestMapping(value = "/{id}/registration", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The admin's access token>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			defaultValue = "Bearer admin",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<Response> listRegistration(@PathVariable UUID id) {
		service.validateCandidate(id);
		Candidate candidate = service.find(id);

		RegistrationStatusDto statusDto = RegistrationStatusDto.of(candidate.getRegistrationStatus());
		Response body = Response
				.Builder()
				.build(Result.SUCCESS)
				.data(statusDto);

		return ResponseEntity.status(HttpStatus.OK).body(body);
	}

	@RequestMapping(value = "/{id}/registration", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.PUT)
	@ApiOperation(value = "Updates the candidate's registration information", response = ResponseBase.class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The admin's access token>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			defaultValue = "Bearer admin",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The candidate's registration is updated!", response = ResponseBase.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<ResponseBase> updateRegistration(@PathVariable UUID id, @Valid @RequestBody RegistrationStatusDto registration) {
		service.updateRegistration(id, registration.getStatus());
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);

		return ResponseEntity.ok().body(responseBase);
	}

	@RequestMapping(value = "/{id}/cv", produces = {"application/json"}, consumes = {"multipart/form-data"}, method = RequestMethod.POST)
	@ApiOperation(value = "Creates the candidate's Curriculum Vitae", response = CreateResponse.class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			defaultValue = "Bearer admin",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The CV is created!", response = ResponseBase.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<ResponseBase> createCV(UriComponentsBuilder uriBuilder, @PathVariable UUID id, @Valid @RequestBody MultipartFile request) throws IOException {
		service.validateCandidate(id);
		validator.validateCV(request);

		Candidate candidate = service.find(id);
		candidate.setCv(new Blob(request.getOriginalFilename(), request.getContentType(), request.getBytes()));
		candidateRepository.save(candidate);

		UriComponents uriComponents =	uriBuilder.path("/{id}/cv").buildAndExpand(id);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);

		return ResponseEntity.created(uriComponents.toUri()).body(responseBase);
	}

	@ApiOperation(value = "Deletes the candidate's image", response = ResponseBase.class)
	@RequestMapping(value = "/{id}/cv", produces = {"application/json"},	method = RequestMethod.DELETE)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			defaultValue = "Bearer admin",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseBase> deleteCV(@PathVariable UUID id) throws NotFoundException {
		service.validateCandidate(id);

		Candidate candidate = service.find(id);
		if (candidate.getImage() == null) {
			throw new NotFoundException("Candidate with id '" + id + "' has not uploaded any CV!");
		}
		candidate.setCv(null);
		candidateRepository.save(candidate);

		return ResponseEntity.ok().body(ResponseBase.Builder().build(Result.SUCCESS));
	}

	@ApiOperation(value = "Lists the candidate's Curriculum Vitae", response = byte[].class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			defaultValue = "Bearer admin",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@RequestMapping(value = "/{id}/cv", produces = {"multipart/form-data"}, method = RequestMethod.GET)
	public ResponseEntity<byte[]> listCV(@PathVariable UUID id) throws NotFoundException {
		service.validateCandidate(id);
		Candidate candidate = service.find(id);
		Blob cv = candidate.getCv();

		if (cv == null) {
			throw new NotFoundException("Candidate with id '" + id + "' has not uploaded a CV yet!");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + cv.getName() + "; preview-type=" + cv.getType());

		return ControllerUtils.listResource(cv.getData(), headers);
	}

	@RequestMapping(value = "/{id}/image", produces = {"application/json"}, consumes = {"multipart/form-data"}, method = RequestMethod.POST)
	@ApiOperation(value = "Creates the candidate's image", response = ResponseBase.class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			defaultValue = "Bearer admin",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The image is created!", response = ResponseBase.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<ResponseBase> createImage(UriComponentsBuilder uriBuilder, @PathVariable UUID id, @Valid @RequestBody MultipartFile request) throws IOException {
		service.validateCandidate(id);
		validator.validateImage(request);

		Candidate candidate = service.find(id);
		candidate.setImage(new Blob(request.getOriginalFilename(), request.getContentType(), request.getBytes()));
		candidateRepository.save(candidate);

		UriComponents uriComponents =	uriBuilder.path("/{id}/image").buildAndExpand(id);
		ResponseBase responseBase = ResponseBase.Builder().build(Result.SUCCESS);

		return ResponseEntity.created(uriComponents.toUri()).body(responseBase);
	}

	@ApiOperation(value = "Deletes the candidate's image", response = ResponseBase.class)
	@RequestMapping(value = "/{id}/image", produces = {"application/json"},	method = RequestMethod.DELETE)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			defaultValue = "Bearer admin",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ResponseBase> deleteImage(@PathVariable UUID id) throws NotFoundException {
		service.validateCandidate(id);

		Candidate candidate = service.find(id);
		if (candidate.getImage() == null) {
			throw new NotFoundException("Candidate with id '" + id + "' has not uploaded any image!");
		}
		candidate.setImage(null);
		candidateRepository.save(candidate);

		return ResponseEntity.ok().body(ResponseBase.Builder().build(Result.SUCCESS));
	}

	@ApiOperation(value = "Lists the candidate's image", response = byte[].class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			defaultValue = "Bearer admin",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@RequestMapping(value = "/{id}/image", produces = {"multipart/form-data"}, method = RequestMethod.GET)
	public ResponseEntity<byte[]> listImage(@PathVariable UUID id) throws NotFoundException {
		service.validateCandidate(id);
		Candidate candidate = service.find(id);
		Blob image = candidate.getImage();

		if (image == null) {
			throw new NotFoundException("Candidate with id '" + id + "' has not uploaded an image yet!");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getName() + "; preview-type=" + image.getType());

		return ControllerUtils.listResource(image.getData(), headers);
	}
}
