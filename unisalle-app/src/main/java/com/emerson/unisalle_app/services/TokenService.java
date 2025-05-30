package com.emerson.unisalle_app.services;

import com.emerson.unisalle_app.models.Token;

public interface TokenService {
    void revokeAllUserTokensByEmail(String email);
    void saveUserToken(String email, String accessToken, String refreshToken);
    void refreshToken(String refreshToken);
    boolean validateToken(String token);
    boolean validateTokenSessionValidity(String accessToken);
}