package com.example.auth_service.service;

import com.example.auth_service.dto.request.AuthLogin;
import com.example.auth_service.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login (AuthLogin login);
}
