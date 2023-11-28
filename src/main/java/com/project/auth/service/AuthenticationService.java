package com.project.auth.service;

import com.project.auth.model.AuthenticationRequest;
import com.project.auth.model.AuthenticationResponse;
import com.project.auth.model.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    String refreshToken(String jwt);
}