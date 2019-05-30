package com.workable.matchmakers.support;

import com.workable.matchmakers.web.dto.Dto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 *  Utilities used by matchmakers' Controllers
 */
public class ControllerUtils {


	public static <T extends Dto> ResponseEntity<T> listResource(List<T> resources) {
		ResponseEntity<T> response;

		if (resources != null  && !resources.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(resources.get(0));
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return response;
	}

	public static <T extends Dto> ResponseEntity<Iterable> listResources(List<T> resources) {
		ResponseEntity<Iterable> response;

		if (resources != null && !resources.isEmpty()) {
			response = ResponseEntity.status(HttpStatus.OK).body(resources);
		} else {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(resources);
		}

		return response;
	}
}
