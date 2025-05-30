package com.emerson.unisalle_app.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "token")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    @Id
    private String id;
    private String accessToken;
    private String email;
    private String refreshToken;
    private boolean isRevoked;
    private boolean isExpired;
    private String userId;
}
