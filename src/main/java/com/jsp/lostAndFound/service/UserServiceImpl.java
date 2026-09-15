package com.jsp.lostAndFound.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jsp.lostAndFound.dto.RegisterRequestDTO;
import com.jsp.lostAndFound.dto.UserDTO;
import com.jsp.lostAndFound.entity.User;
import com.jsp.lostAndFound.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(RegisterRequestDTO registerRequestDTO) {

        User user = new User();

        user.setName(registerRequestDTO.getName());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(registerRequestDTO.getPassword());
        user.setPhone(registerRequestDTO.getPhone());
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        User updatedUser = userRepository.save(user);

        return convertToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        userRepository.delete(user);
    }

    private UserDTO convertToDTO(User user) {

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}