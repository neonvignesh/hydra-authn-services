package com.enrich.hydra.authn.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.enrich.hydra.authn.constant.Constants;
import com.enrich.hydra.authn.pojo.StandardResponseMessage;
import com.enrich.hydra.authn.provider.HydraServiceProvider;
import com.enrich.hydra.authn.util.StandardResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionFilter implements Filter {

	@Autowired
	private HydraServiceProvider provider;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest http = (HttpServletRequest) request;
		String requestURI = http.getRequestURI();
		String xApiKey = http.getHeader(Constants.X_API_KEY);
		if ((requestURI.contains("swagger")) || (requestURI.contains("/v2/api-docs"))
				|| (requestURI.contains("/health"))) {
			chain.doFilter(request, response);
		} else {
			if (isValidApiKey(xApiKey)) {
				chain.doFilter(request, response);
			} else {
				setUnAuthorizedException((HttpServletResponse) response);
				
			}
		}
	}

	private boolean isValidApiKey(String apiKey) {
		return apiKey != null && apiKey.equals(provider.getApiKey());
	}

	public static StandardResponseMessage setUnAuthorizedException(HttpServletResponse response) throws IOException {
		StandardResponseMessage standardResponse = new StandardResponseMessage();
		standardResponse = StandardResponseUtil.prepareForbiddenResponse(standardResponse);
	    response.setContentType(Constants.APPLICATION_JSON);
	    response.setStatus(HttpStatus.FORBIDDEN.value());
	    ObjectMapper objectMapper = new ObjectMapper();
	    String jsonResponse = objectMapper.writeValueAsString(standardResponse);
	    response.getWriter().write(jsonResponse);
	    return standardResponse;
	}
}
