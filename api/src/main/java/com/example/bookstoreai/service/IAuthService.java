package com.example.bookstoreai.service;

import com.example.bookstoreai.dto.request.LoginRequest;
import com.example.bookstoreai.dto.request.RegisterRequest;
import com.example.bookstoreai.dto.response.AuthResponse;

public interface IAuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse register(RegisterRequest request);
}
