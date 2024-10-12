package com.nhnacademy.miniDooray.provider;

import com.nhnacademy.miniDooray.dto.LoginRequest;
import com.nhnacademy.miniDooray.util.RestApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collections;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private RestApiClient restApiClient;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            String url = "http://localhost:8081/member/login";
            LoginRequest request = new LoginRequest(id, password);
            ResponseEntity<Void> response = restApiClient.sendPostRequest(url, request, Void.class);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
                List<GrantedAuthority> authorities = Collections.emptyList();
                return new UsernamePasswordAuthenticationToken(id, password, authorities);
            } else {
                throw new BadCredentialsException("Invalid credentials");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 400) {
                throw new BadCredentialsException("Bad request: Invalid data format", e);
            } else if (e.getStatusCode().value() == 401) {
                throw new BadCredentialsException("Unauthorized: Invalid credentials", e);
            } else {
                throw new BadCredentialsException("Authentication failed", e);
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}