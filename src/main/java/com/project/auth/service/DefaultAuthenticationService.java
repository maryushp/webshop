package com.project.auth.service;

import com.project.auth.model.AuthenticationRequest;
import com.project.auth.model.AuthenticationResponse;
import com.project.auth.model.RegisterRequest;
import com.project.jwt.JwtService;
import com.project.user.model.Role;
import com.project.user.model.User;
import com.project.user.repository.UserRepository;
import com.project.utils.exceptionhandler.exceptions.ElementNotFoundException;
import com.project.utils.exceptionhandler.exceptions.InvalidTokenException;
import com.project.utils.exceptionhandler.exceptions.SuchElementAlreadyExists;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class DefaultAuthenticationService implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailService;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getFirstname())
                .surname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new SuchElementAlreadyExists(MessageFormat.format(SUCH_USER_EXISTS, user.getEmail()));
        }

        userRepository.save(user);

        return buildAuthenticationResponse(user);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword()));
        User user =
                userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new ElementNotFoundException(USER_NOT_FOUND));

        return buildAuthenticationResponse(user);
    }

    @Override
    public String refreshToken(String jwt) {
        String userEmail = jwtService.extractUsername(jwt);
        if (userEmail == null) {
            throw new InvalidTokenException(INVALID_USERNAME);
        }
        UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);
        if (!jwtService.isTokenValid(jwt, userDetails)) {
            throw new InvalidTokenException(INVALID_REFRESH_TOKEN);
        }
        return jwtService.generateAccessToken(userDetails);
    }

    private AuthenticationResponse buildAuthenticationResponse(User user) {
        var jwtAccessToken = jwtService.generateAccessToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .id(user.getId())
                .build();
    }
}