package com.workable.matchmakers.web.api.v1;

import io.swagger.annotations.*;
import javassist.NotFoundException;
import com.workable.matchmakers.service.UserService;
import com.workable.matchmakers.support.ControllerUtils;
import com.workable.matchmakers.support.LoggingHelper;
import com.workable.matchmakers.web.api.BaseMatchmakersController;
import com.workable.matchmakers.web.dto.IdentityDto;
import com.workable.matchmakers.web.dto.patch.PatchRequest;
import com.workable.matchmakers.web.dto.response.CreateResponse;
import com.workable.matchmakers.web.dto.response.CreateResponseData;
import com.workable.matchmakers.web.dto.users.UserBaseDto;
import com.workable.matchmakers.web.dto.users.UserDto;
import com.workable.matchmakers.web.enums.Result;
import com.workable.matchmakers.web.validator.PatchValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Api(value = "User API", tags = "Users", position = 2, description = "User Management")
@RequestMapping(value = "/users")
public class UserController extends BaseMatchmakersController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserService service;

	@RequestMapping(value = "", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.POST)
	@ApiOperation(value = "Creates the user", response = CreateResponse.class)
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The user is created!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<CreateResponse> create(UriComponentsBuilder uriBuilder, @Valid @RequestBody UserBaseDto userBaseDto) {
		ResponseEntity<CreateResponse> response;
		logger.debug(LoggingHelper.logInboundRequest(userBaseDto));

		CreateResponseData data = service.create(userBaseDto);
		UriComponents uriComponents =	uriBuilder.path("/users/{id}").buildAndExpand(data.getId());
		CreateResponse responseBase = CreateResponse.Builder().build(Result.SUCCESS).data(data);
		response = ResponseEntity.created(uriComponents.toUri()).body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@RequestMapping(value = "/{id}", produces = {"application/json"}, consumes = {"application/json"},	method = RequestMethod.PATCH)
	@ApiOperation(value = "Updates the user's information", response = CreateResponse.class)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The user is updated!", response = CreateResponse.class),
			@ApiResponse(code = 400, message = "The request is invalid!"),
			@ApiResponse(code = 500, message = "server error")})
	public ResponseEntity<CreateResponse> update(@PathVariable UUID id, @Valid @RequestBody PatchRequest request) {
		ResponseEntity<CreateResponse> response;
		logger.debug(LoggingHelper.logInboundRequest("/users/" + id + "\n" + request));

		PatchValidator.validatePatchRequest(request, UserDto.fields);

		CreateResponseData data = service.update(id, request.getPatches());
		CreateResponse responseBase = CreateResponse.Builder().build(Result.SUCCESS).data(data);
		response = ResponseEntity.ok().body(responseBase);

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Authenticates the user returning his identification", response = IdentityDto.class)
	@RequestMapping(value = "/id", produces = {"application/json"},	method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The user is authenticated", response = IdentityDto.class),
			@ApiResponse(code = 401, message = "The credentials are invalid"),
			@ApiResponse(code = 404, message = "No user found!"),
			@ApiResponse(code = 500, message = "Server Error")})
	public ResponseEntity<IdentityDto> listAccount(UriComponentsBuilder uriBuilder, @RequestParam String username, @RequestParam String password) throws NotFoundException {

		logger.debug(LoggingHelper.logInboundRequest("/users?username=" + username + "?password=" + password));

		ResponseEntity<IdentityDto> response;
		UserDto account = service.find(username, password);

		if (account == null) {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			IdentityDto identityDto = new IdentityDto(account.getId().toString());
			UriComponents uriComponents = uriBuilder.path("/users/{id}").buildAndExpand(identityDto.getId());
			response = ResponseEntity.status(HttpStatus.OK).location(uriComponents.toUri()).body(identityDto);
		}

		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists all users", response = UserDto.class, responseContainer="List")
	@RequestMapping(value = "", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<Iterable> listAllAccounts() {

		logger.debug(LoggingHelper.logInboundRequest("/users"));
		ResponseEntity<Iterable> response = listAll();
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	@ApiOperation(value = "Lists the user", response = UserDto.class)
	@RequestMapping(value = "/{id}", produces = {"application/json"},	method = RequestMethod.GET)
	@ApiImplicitParam(
			name = "Authorization",
			value = "Bearer <The user's access token obtained upon registration or authentication>",
			example = "Bearer 6b6f2985-ae5b-46bc-bad1-f9176ab90171",
			required = true,
			dataType = "string",
			paramType = "header"
	)
	public ResponseEntity<UserDto> listAccount(@PathVariable UUID id) {

		logger.debug(LoggingHelper.logInboundRequest("/users/" + id));
		ResponseEntity<UserDto> response = list(id);
		logger.debug(LoggingHelper.logInboundResponse(response));

		return response;
	}

	private ResponseEntity<Iterable> listAll() {
		List<UserDto> users = service.listAll();

		return ControllerUtils.listResources(users);
	}

	private ResponseEntity<UserDto> list(String username) {
		List<UserDto> users = service.list(username);

		return ControllerUtils.listResource(users);
	}

	private ResponseEntity<UserDto> list(UUID id) {
		List<UserDto> users = service.list(id);

		return ControllerUtils.listResource(users);
	}
}
