package com.jsp.lostAndFound.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jsp.lostAndFound.dto.LoginRequestDTO;
import com.jsp.lostAndFound.dto.LoginResponseDTO;
import com.jsp.lostAndFound.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO requestDTO) {

        LoginResponseDTO response = authService.login(requestDTO);

        return ResponseEntity.ok(response);
    }
}