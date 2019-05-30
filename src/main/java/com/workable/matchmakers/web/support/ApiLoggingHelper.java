package com.workable.matchmakers.web.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
public class ApiLoggingHelper {

    /** The Constant HORIZONTAL_LINE. */
    private static final String HORIZONTAL_LINE = "----------------------------------";

    /**
     * Serialize the request for investigation / debugging purposes.
     *
     * @param request
     *            the request
     * @return the string
     */
    public static String serializeRequest(HttpServletRequest request, boolean isBinary) {
        StrBuilder text = new StrBuilder();

        text.appendln(HORIZONTAL_LINE);
        text.appendln("Request path: " + request.getMethod() + " " + request.getRequestURL().toString());
        text.appendln(HORIZONTAL_LINE);
        text.appendln("Headers");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            text.appendln(headerName + ": " + request.getHeader(headerName));
        }
        text.appendln(HORIZONTAL_LINE);

        if (!isBinary) {
            text.appendln("Body");
            try {
                text.appendln(IOUtils.toString(request.getInputStream()));
            } catch (IOException e) {
                String error = "COULD NOT SERIALIZE BODY TO TEXT!";
                log.error(error, e);
                text.append(error);
            }
            for (Map.Entry<String, String[]> paramEntry : request.getParameterMap().entrySet()) {
                text.appendln(paramEntry.getKey() + "= " + StringUtils.join(paramEntry.getValue()));
            }
            text.appendln(HORIZONTAL_LINE);
        }


        return text.toString();
    }

    /**
     * Serialize response.
     *
     * @param response the response
     * @param baos the baos
     * @return the string
     */
    public static String serializeResponse(HttpServletResponse response, OutputStream baos, boolean isBinary) {
        StrBuilder text = new StrBuilder();

        text.appendln(HORIZONTAL_LINE);
        text.appendln("Status: " + response.getStatus());
        text.appendln(HORIZONTAL_LINE);
        text.appendln("Headers");

        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            text.appendln(headerName + ": " + response.getHeader(headerName));
        }
        text.appendln(HORIZONTAL_LINE);

        if (!isBinary) {
            text.appendln("Body");
            text.appendln(baos.toString());
            // text.appendln(new String(baos.toByteArray(), "UTF-8"));
            text.appendln(HORIZONTAL_LINE);
        }

        return text.toString();
    }


    public static String logInboundRequest(Object entity) {
        return "<-- REQ: " + entity;
    }

    public static String logInboundResponse(Object entity) {
        return "--> RES: " + entity;
    }

    public static String logOutboundRequest(Object entity) {
        return "--> REQ: " + entity;
    }

    public static String logOutboundResponse(Object entity) {
        return "<-- RES: " + entity;
    }
}
