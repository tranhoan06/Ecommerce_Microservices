package com.example.auth_service.client.impl;

import com.example.auth_service.client.AuthClient;
import com.example.auth_service.dto.request.AuthLogin;
import com.example.auth_service.dto.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AuthClientImpl implements AuthClient {
    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    private final WebClient.Builder webClientBuilder;

    @Override
    public AuthResponse loginAuthClient(AuthLogin authLogin) {
        AuthResponse response = webClientBuilder
                .build()
                .post()
                .uri("http://localhost:8080/realms/EcommerceMicroservices/protocol/openid-connect/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(
                        BodyInserters.fromFormData("client_id", "backend")
                                .with("username", authLogin.getUsername())
                                .with("password", authLogin.getPassword())
                                .with("grant_type", "password")
                                .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();

        if (response == null) {
            throw new RuntimeException("Login thất bại");
        }

        return response;
    }
}
