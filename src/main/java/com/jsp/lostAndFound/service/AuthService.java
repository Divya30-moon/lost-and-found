package com.jsp.lostAndFound.service;

import com.jsp.lostAndFound.dto.LoginRequestDTO;
import com.jsp.lostAndFound.dto.LoginResponseDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO requestDTO);
}