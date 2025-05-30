package com.emerson.unisalle_app.services;

import com.emerson.unisalle_app.controller.dto.AuthResponse;
import com.emerson.unisalle_app.controller.dto.SignupRequest;
import com.emerson.unisalle_app.controller.dto.UserDto;

public interface UserMngService {
    AuthResponse createUser(SignupRequest signupRequest);
    void logout(String email);
    UserDto getUserDetails(String email);
}
