package com.jsp.lostAndFound.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.jsp.lostAndFound.dto.LoginRequestDTO;
import com.jsp.lostAndFound.dto.LoginResponseDTO;
import com.jsp.lostAndFound.entity.User;
import com.jsp.lostAndFound.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDTO) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getEmail(),
                        requestDTO.getPassword()
                )
        );

        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User with email "
                                + requestDTO.getEmail()
                                + " not found"
                        )
                );

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return new LoginResponseDTO(
                "Login successful",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}