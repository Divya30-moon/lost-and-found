package com.jsp.lostAndFound.service;

import java.util.List;

import com.jsp.lostAndFound.dto.RegisterRequestDTO;
import com.jsp.lostAndFound.dto.UpdateUserRequestDTO;
import com.jsp.lostAndFound.dto.UserDTO;

public interface UserService {

	UserDTO createUser(RegisterRequestDTO registerRequestDTO);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(Long id, UpdateUserRequestDTO updateUserRequestDTO);

    void deleteUser(Long id);
}