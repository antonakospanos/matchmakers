package com.workable.matchmakers.web.support;

import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MutableHttpServletRequest extends HttpServletRequestWrapper {

	private byte[] payload;

	public MutableHttpServletRequest(HttpServletRequest request) throws IOException {
		super(request);
		this.payload = StreamUtils.copyToByteArray(request.getInputStream());
	}

	@Override
    public ServletInputStream getInputStream () throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.payload);
        ServletInputStream inputStream = new DelegatingServletInputStream(byteArrayInputStream);
        return inputStream;
    }
}
