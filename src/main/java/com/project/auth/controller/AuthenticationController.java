package com.project.auth.controller;

import com.project.auth.model.AuthenticationRequest;
import com.project.auth.model.AuthenticationResponse;
import com.project.auth.model.RegisterRequest;
import com.project.auth.service.DefaultAuthenticationService;
import com.project.utils.exceptionhandler.exceptions.InvalidTokenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final DefaultAuthenticationService authService;
    public static final String AUTHORIZATION = "Authorization";

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String jwt;
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new InvalidTokenException(REQUEST_SHOULD_CONTAIN_TOKEN);
        }
        jwt = bearerToken.substring(7);
        String token = authService.refreshToken(jwt);
        response.setHeader(AUTHORIZATION,
                "Bearer " + token);
        return ResponseEntity.noContent().build();
    }
}