package com.workable.matchmakers.web.validator;

import com.workable.matchmakers.web.dto.patch.PatchOperation;
import com.workable.matchmakers.web.dto.patch.PatchRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PatchValidator {

	public static void validatePatchRequest(PatchRequest request, List<String> fields) {

		request.getPatches().stream().forEach(patchDto -> {

			String field = patchDto.getField();
			PatchOperation operation = patchDto.getOperation();
			String value = patchDto.getValue();

			// Field validation
			if (StringUtils.isBlank(field)) {
				throw new IllegalArgumentException("Field is required! Resource: " + fields);
			} else if (!fields.contains(field)) {
				throw new IllegalArgumentException("Field '" + field + "' is not included in resource: " + fields);
			}

			// Value validation
			if (PatchOperation.ADD.equals(operation) && StringUtils.isEmpty(value)) {
				throw new IllegalArgumentException("Value '" + value + "' is required in case of ADD operation");
			}
			if (PatchOperation.REPLACE.equals(operation) && StringUtils.isEmpty(value)) {
				throw new IllegalArgumentException("Value '" + value + "' is required in case of REPLACE operation");
			}
			if (PatchOperation.REMOVE.equals(operation) && StringUtils.isNotBlank(value)) {
				throw new IllegalArgumentException("Value '" + value + "' should be avoided in case of REMOVE operation");
			}

			// Operation validation
			if (PatchOperation.REPLACE != operation) {
				throw new IllegalArgumentException("Patch operation '"+operation+"' is not supported!");
			}
		});
	}
}
