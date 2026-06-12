package com.example.auth_service.client;

import com.example.auth_service.dto.request.AuthLogin;
import com.example.auth_service.dto.response.AuthResponse;

public interface AuthClient {
    AuthResponse loginAuthClient(AuthLogin authLogin);
}
