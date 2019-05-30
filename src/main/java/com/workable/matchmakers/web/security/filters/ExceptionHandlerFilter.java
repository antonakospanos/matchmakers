package com.workable.matchmakers.web.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workable.matchmakers.web.dto.response.ResponseBase;
import com.workable.matchmakers.web.enums.Result;
import com.workable.matchmakers.web.dto.response.ResponseBase;
import com.workable.matchmakers.web.enums.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handles exception thrown by Filters before Spring MVC takes control. This
 * mostly covers HTTP Header validations for fields that are required for
 * preprocessing (e.g. authentication).
 */
public class ExceptionHandlerFilter extends GenericFilterBean {

    private final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerFilter.class);

    private static final ObjectMapper jsonSerializer = new ObjectMapper();

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception exc) {
            handleHeaders((HttpServletRequest) request, (HttpServletResponse) response, exc);
        }

    }

    /**
     * Handle header exception.
     *
     * @param request the request
     * @param response the response
     * @param exec the exception
     */
    private void handleHeaders(HttpServletRequest request, HttpServletResponse response, Exception exec) throws IOException {
        LOGGER.error("Error validating request: " + request.getRequestURL() + ". Cause: '" + exec.getMessage() + "'");

        ResponseBase body = new ResponseBase();
        body.setResult(Result.UNAUTHORIZED);
        body.setDescription(exec.getMessage());

        respondError(response, body, HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Respond error.
     *
     * @param response the response
     * @param body the error body
     * @param httpStatus the http status
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void respondError(HttpServletResponse response, ResponseBase body, Integer httpStatus) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);

        ServletOutputStream os = response.getOutputStream();
        try {
            String serializedError = jsonSerializer.writeValueAsString(body);
            os.print(serializedError);
        } finally {
            os.close();
        }
    }
}
