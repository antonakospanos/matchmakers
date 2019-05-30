package com.workable.matchmakers.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Component
public class BlobValidator {

    private static final String[] IMAGE_TYPES = {
            "image/jpeg",
            "image/png",
            "image/gif"
    };
    private static final String[] CV_TYPES = {
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.oasis.opendocument.text"
    };

    public void validateImage(MultipartFile request) {
        if (!Arrays.asList(IMAGE_TYPES).contains(request.getContentType())) {
            throw new IllegalArgumentException("Image '" + request.getOriginalFilename() + "' is not JPEG, PNG or GIF");
        }
    }

    public void validateCV(MultipartFile request) {
        if (!Arrays.asList(CV_TYPES).contains(request.getContentType())) {
            throw new IllegalArgumentException("CV '" + request.getOriginalFilename() + "' is not PDF, DOC, DOCX or ODT");
        }
    }
}
