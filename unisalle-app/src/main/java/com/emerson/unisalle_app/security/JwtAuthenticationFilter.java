package com.emerson.unisalle_app.security;

import com.emerson.unisalle_app.controller.dto.AuthResponse;
import com.emerson.unisalle_app.controller.dto.AuthenticationRequestDTO;
import com.emerson.unisalle_app.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(ObjectMapper mapper, JwtUtils jwtUtils, AuthenticationManager authenticationManager, TokenService tokenService){
        this.mapper = mapper;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
        super.setFilterProcessesUrl("/api/login");
        super.setAuthenticationManager(authenticationManager);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{

        AuthenticationRequestDTO auth = null;
        try {
            auth = mapper.readValue(request.getInputStream(), AuthenticationRequestDTO.class);
        } catch (IOException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        return  getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword()));
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserSecurityDetails userSecurityDetails =   (UserSecurityDetails) authResult.getPrincipal();
        String subject = userSecurityDetails.getUsername();
        System.out.println("GENERITING TOKENS FOR THE FRONTEND");
        String accessToken = jwtUtils.generateAccessToken(
                userSecurityDetails.getUsername(),
                Map.of("authorities", userSecurityDetails
                        .getAuthorities()
                        .stream()
                        .map(Object::toString)
                        .toList()));
        String refreshToken = jwtUtils.generateRefreshToken(userSecurityDetails.getUsername());

        tokenService.revokeAllUserTokensByEmail(subject);
        tokenService.saveUserToken(subject, accessToken, refreshToken);



        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(AuthResponse.builder().accessToken(accessToken).build()));
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
