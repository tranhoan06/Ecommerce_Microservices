package com.example.auth_service.service.impl;

import com.example.auth_service.client.AuthClient;
import com.example.auth_service.dto.request.AuthLogin;
import com.example.auth_service.dto.response.AuthResponse;
import com.example.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthClient authClient;

    @Override
    public AuthResponse login(AuthLogin login) {
        AuthResponse response = authClient.loginAuthClient(login);
        log.info("response {}:", response);
        return response;
    }
}
