package br.com.brainyit.posystem2.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SecurityHandler {

    Jwt getJwt() {
        final var authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            return (Jwt) authentication.getPrincipal();
        }
        return null;
    }

    public String getUserName() {
        final var jwt = getJwt();
        if (jwt == null) {
            return StringUtils.EMPTY;
        }
        return jwt.getClaim("preferred_username");
    }
}
