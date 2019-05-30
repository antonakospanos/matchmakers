package com.workable.matchmakers.web.support;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.util.List;

/**
 *  Utilities used by matchmakers' Controllers
 */
public class ControllerUtils {


	public static <T extends Serializable> ResponseEntity<T> listResource(T resource, HttpHeaders headers) {
		ResponseEntity<T> response;

		if (resource != null) {
			response = ResponseEntity.status(HttpStatus.OK).headers(headers).body(resource);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body(null);
		}

		return response;
	}

	public static <T extends Serializable> ResponseEntity<T> listResource(List<T> resources) {
		ResponseEntity<T> response;

		if (resources != null && !resources.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(resources.get(0));
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}


		return response;
	}

	public static <T extends Serializable> ResponseEntity<Iterable> listResources(List<T> resources) {
		ResponseEntity<Iterable> response;

		if (resources != null && !resources.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(resources);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(resources);
		}

		return response;
	}
}
