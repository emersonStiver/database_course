package com.emerson.unisalle_app.services.imp;

import com.emerson.unisalle_app.controller.dto.AuthResponse;
import com.emerson.unisalle_app.controller.dto.SignupRequest;
import com.emerson.unisalle_app.controller.dto.UserDto;
import com.emerson.unisalle_app.models.User;
import com.emerson.unisalle_app.repositories.UserRepository;
import com.emerson.unisalle_app.security.JwtUtils;
import com.emerson.unisalle_app.services.TokenService;
import com.emerson.unisalle_app.services.UserMngService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserMngServiceImp implements UserMngService {
    private final UserRepository userRepository;
    private final TokenService  tokenService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    @Override
    public AuthResponse createUser(SignupRequest signupRequest){
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("User already exists");
        }

        User user = userRepository.save(mapToUser(signupRequest));
        if(user.getId() != null) {
            System.out.println(user.getAuthorities());
            String accessToken = jwtUtils.generateAccessToken(user.getEmail(), Map.of("authorities", user.getAuthorities()));
            tokenService.saveUserToken(user.getEmail(), accessToken, "");
            return mapToAuthResponse(user, accessToken);
        } else {
            throw new RuntimeException("User creation failed");
        }
    }

    private User mapToUser(SignupRequest signupRequest){
        return User.builder()
                .email(signupRequest.getEmail())
                .name(signupRequest.getName())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .authorities("ROLE_USER")
                .build();
    }

    @Override
    public void logout(String email ){
        tokenService.revokeAllUserTokensByEmail(email);
    }

    @Override
    public UserDto getUserDetails(String email){
        return userRepository.findByEmail(email)
                .map(this::mapToUserDto)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
    private AuthResponse mapToAuthResponse(User user, String accessToken) {
        return AuthResponse.builder()
                .email(user.getEmail())
                .accessToken(accessToken)
                .build();
    }



}
