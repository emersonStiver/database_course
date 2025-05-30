package com.emerson.unisalle_app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "myapp.security.jwt")
public class CustomProperties {
    private String secretKey;
    private long accessTokenExpirationTime;
    private long refreshTokenExpirationTime;
    private List<String> allowedRoutes;
}
