package com.emerson.unisalle_app.controller.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupRequest {
    private String email;
    private String password;
    private String name;
}
