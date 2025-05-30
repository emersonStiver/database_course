package com.emerson.unisalle_app.security;

import com.emerson.unisalle_app.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;
    public JwtAuthorizationFilter(JwtUtils jwtUtils, TokenService tokenService){
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

        if(request.getServletPath().contains("/login")){
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader("Authorization");
        if(accessToken == null  || !accessToken.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        String substringAccessToken = accessToken.substring(7);
        final String subject = jwtUtils.extractSubject(substringAccessToken);
        var authorities = jwtUtils.extractAuthorities(substringAccessToken);

        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean isTokenValid = tokenService.validateTokenSessionValidity(substringAccessToken);


            if (isTokenValid && jwtUtils.validateTokenExpiration(substringAccessToken) && tokenService.validateToken(substringAccessToken)) {
                var authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                        subject,
                        null, // no password needed here
                        authorities
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request,response);
    }
}
