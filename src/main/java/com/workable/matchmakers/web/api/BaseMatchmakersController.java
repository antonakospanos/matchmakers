package com.workable.matchmakers.web.api;

import javassist.NotFoundException;
import com.workable.matchmakers.web.dto.response.ResponseBase;
import com.workable.matchmakers.web.enums.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class BaseMatchmakersController {

	private final static Logger logger = LoggerFactory.getLogger(BaseMatchmakersController.class);

	protected final static String RESPONSE = "Not available yet!";

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseBase> handleMissingParams(MissingServletRequestParameterException exception) {
		ResponseBase validationError = buildErrorResponse(exception, Result.BAD_REQUEST);
		validationError.setDescription("The " + exception.getParameterName() + " parameter of the request is required!");

		logger.error(validationError.getResult() + ": " + validationError.getDescription(), exception);

		return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseBase> handleValidationException(IllegalArgumentException exception) {
		ResponseBase validationError = buildErrorResponse(exception, Result.BAD_REQUEST);
		logger.error(exception.getClass() + " Cause: " + exception.getCause() + " Message: " + exception.getMessage());

		return new ResponseEntity<>(validationError, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AuthorizationServiceException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ResponseBase> handleAuthorizationException(AuthorizationServiceException exception) {
		ResponseBase authorizationError = buildErrorResponse(exception, Result.UNAUTHORIZED);
		logger.error(exception.getClass() + " Cause: " + exception.getCause() + " Message: " + exception.getMessage());

		return new ResponseEntity<>(authorizationError, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ResponseBase> handleAuthorizationException(NotFoundException exception) {
		ResponseBase resourceError = buildErrorResponse(exception, Result.NOT_FOUND);
		logger.error(exception.getClass() + " Cause: " + exception.getCause() + " Message: " + exception.getMessage());

		return new ResponseEntity<>(resourceError, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ResponseBase> handleInternalServerError(Exception exception) {
		ResponseBase resourceError = buildErrorResponse(exception, Result.GENERIC_ERROR);
		logger.error(exception.getClass() + " Cause: " + exception.getCause() + " Message: " + exception.getMessage()
				+ " Stack Trace: ", exception);

		return new ResponseEntity<>(resourceError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Builds the HTTP response in case of an exceptional handling
	 *
	 * @param exception
	 * @param result
	 * @return The HTTP response body
	 */
	private ResponseBase buildErrorResponse(Exception exception, Result result) {
		ResponseBase response = new ResponseBase();
		response.setResult(result);
		response.setDescription(exception.getMessage());

		return response;
	}
}
