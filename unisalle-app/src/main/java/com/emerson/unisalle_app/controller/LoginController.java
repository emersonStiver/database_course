package com.emerson.unisalle_app.controller;


import com.emerson.unisalle_app.controller.dto.AuthResponse;
import com.emerson.unisalle_app.controller.dto.SignupRequest;
import com.emerson.unisalle_app.controller.dto.UserDto;
import com.emerson.unisalle_app.services.UserMngService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
public class LoginController {

    private final UserMngService userMngService;

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest signupRequest){
        AuthResponse authResponse = userMngService.createUser(signupRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal String email){
        userMngService.logout(email);
        return ResponseEntity.ok(true);
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<UserDto> profile(@AuthenticationPrincipal String email){
        return ResponseEntity.ok(userMngService.getUserDetails(email));
    }
}
