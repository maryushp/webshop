package com.project.auth.service;

import com.project.auth.model.AuthenticationRequest;
import com.project.auth.model.AuthenticationResponse;
import com.project.auth.model.RegisterRequest;
import com.project.jwt.JwtService;
import com.project.user.model.User;
import com.project.user.repository.UserRepository;
import com.project.utils.exceptionhandler.exceptions.ElementNotFoundException;
import com.project.utils.exceptionhandler.exceptions.InvalidTokenException;
import com.project.utils.exceptionhandler.exceptions.SuchElementAlreadyExists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultAuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailService;
    @InjectMocks
    private DefaultAuthenticationService defaultAuthenticationService;

    private final String accessToken = "accessToken";
    private final String refreshToken = "refreshToken";
    private final String jwt = "jwt";

    private static RegisterRequest registerRequest;
    private static AuthenticationRequest authenticationRequest;
    private static User user;


    @BeforeAll
    static void setUp() {
        registerRequest = RegisterRequest.builder()
                .email("email@example.com")
                .firstname("firstname")
                .lastname("lastname")
                .password("password")
                .build();

        user = User.builder()
                .email("email@example.com")
                .name("firstname")
                .surname("lastname")
                .password("password")
                .build();

        authenticationRequest = AuthenticationRequest.builder()
                .email("email@example.com")
                .password("password")
                .build();
    }


    @Test
    void registerSuchUserExists() {
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(registerRequest.getPassword());

        assertThrows(SuchElementAlreadyExists.class, () -> defaultAuthenticationService.register(registerRequest));
    }

    @Test
    void registerSuccess() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        when(jwtService.generateAccessToken(any())).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(any())).thenReturn(refreshToken);

        AuthenticationResponse authenticationResponse = defaultAuthenticationService.register(registerRequest);

        assertEquals(List.of(accessToken, refreshToken), List.of(authenticationResponse.getAccessToken(),
                authenticationResponse.getRefreshToken()));
    }

    @Test
    void authenticateUserNotFound() {
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class,
                () -> defaultAuthenticationService.authenticate(authenticationRequest));
    }

    @Test
    void authenticateInvalidPassword() {
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("") {
        });

        assertThrows(AuthenticationException.class,
                () -> defaultAuthenticationService.authenticate(authenticationRequest));
    }


    @Test
    void authenticateSuccess() {
        when(userRepository.findByEmail(authenticationRequest.getEmail())).thenReturn(Optional.of(user));

        when(jwtService.generateAccessToken(any())).thenReturn(accessToken);
        when(jwtService.generateRefreshToken(any())).thenReturn(refreshToken);

        AuthenticationResponse authenticationResponse =
                defaultAuthenticationService.authenticate(authenticationRequest);

        assertEquals(List.of(accessToken, refreshToken), List.of(authenticationResponse.getAccessToken(),
                authenticationResponse.getRefreshToken()));
    }

    @Test
    void refreshTokenInvalidUsername() {
        when(jwtService.extractUsername(jwt)).thenReturn(null);

        assertThrows(InvalidTokenException.class, () -> defaultAuthenticationService.refreshToken(jwt));
    }

    @Test
    void refreshTokenInvalidToken() {
        when(jwtService.extractUsername(jwt)).thenReturn(user.getEmail());
        when(userDetailService.loadUserByUsername(user.getEmail())).thenReturn(user);
        when(jwtService.isTokenValid(jwt, user)).thenReturn(false);

        assertThrows(InvalidTokenException.class, () -> defaultAuthenticationService.refreshToken(jwt));
    }

    @Test
    void refreshTokenSuccess() {
        when(jwtService.extractUsername(jwt)).thenReturn(user.getEmail());
        when(userDetailService.loadUserByUsername(user.getEmail())).thenReturn(user);
        when(jwtService.isTokenValid(jwt, user)).thenReturn(true);
        when(jwtService.generateAccessToken(user)).thenReturn(accessToken);

        assertEquals(accessToken, defaultAuthenticationService.refreshToken(jwt));
    }
}