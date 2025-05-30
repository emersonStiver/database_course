package com.emerson.unisalle_app.services.imp;

import com.emerson.unisalle_app.exceptions.ResourceNotFoundException;
import com.emerson.unisalle_app.models.Token;
import com.emerson.unisalle_app.models.User;
import com.emerson.unisalle_app.repositories.TokenRepository;
import com.emerson.unisalle_app.repositories.UserRepository;
import com.emerson.unisalle_app.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenServiceImp implements TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public void revokeAllUserTokensByEmail(String email){
        List<Token> tokens = tokenRepository.findAllByEmail(email);
        if(tokens.isEmpty()) return;
        for(Token token : tokens){
            token.setRevoked(true);
            token.setExpired(true);
        }
        tokenRepository.saveAll(tokens);
    }

    @Override
    public void saveUserToken(String email, String accessToken, String refreshToken){
        System.out.println("SAVING A NEW TOKEN: " + accessToken);
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(email)
                .userId(email)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);
    }



    @Override
    public void refreshToken(String refreshToken){

    }

    @Override
    public boolean validateToken(String accessToken){
        Token t = tokenRepository
                .findByAccessToken(accessToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token " + accessToken +" was not found"));
        return !t.isExpired() && !t.isRevoked();
    }

    @Override
    public boolean validateTokenSessionValidity(String accessToken){
        return this.tokenRepository.findByAccessToken(accessToken).map(token -> !token.isRevoked() && !token.isExpired()).orElse(false);
    }
}
