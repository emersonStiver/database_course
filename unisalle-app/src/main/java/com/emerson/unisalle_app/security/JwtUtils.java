package com.emerson.unisalle_app.security;

import com.emerson.unisalle_app.config.CustomProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final CustomProperties customProperties;

    public String generateAccessToken(String subject, Map<String, Object> extraClaims){
        return buildToken(subject, extraClaims, customProperties.getAccessTokenExpirationTime());
    }

    public String generateRefreshToken(String subject){
        return buildToken(subject, null, customProperties.getRefreshTokenExpirationTime());
    }

    public boolean validateTokenExpiration(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // SignatureException, MalformedJwtException, ExpiredJwtException, etc.
            return false;
        }
    }

    public String extractSubject(String token){
        return extractAllClaims(token).getSubject();
    }

    public List<GrantedAuthority> extractAuthorities(String token) {
        Claims claims = extractAllClaims(token);
        Object authoritiesClaim = claims.get("authorities");

        if (authoritiesClaim instanceof String singleAuthority) {
            return List.of(new SimpleGrantedAuthority(singleAuthority));
        }

        if (authoritiesClaim instanceof List<?> authorityList) {
            return authorityList.stream()
                    .filter(Objects::nonNull)
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }




    //Utility methods
    private String buildToken(String subject, Map<String, Object> extraClaims, long expiration){
        return Jwts
                .builder()
                .setSubject(subject)
                .addClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(keyGenerator(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Claims extractAllClaims(String token){
        //We initialize the parse using the key used to sign the JWT in the first place
        JwtParser jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(keyGenerator())
                .build();
        return jwtParser.parseClaimsJws(token).getBody();
    }

    private Key keyGenerator(){
        byte[] keyBytes = customProperties.getSecretKey().getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
