package com.naveen.microservice.wordwrap.controller.security;

import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

public final class ApiKeyFetchFilter extends AbstractPreAuthenticatedProcessingFilter {
    private static final String AUTH_KEY_HEADER_NAME = "api_key";

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(AUTH_KEY_HEADER_NAME);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return "N/A";
    }
}
