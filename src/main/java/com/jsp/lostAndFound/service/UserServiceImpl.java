package com.jsp.lostAndFound.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jsp.lostAndFound.config.SecurityUtil;
import com.jsp.lostAndFound.dto.RegisterRequestDTO;
import com.jsp.lostAndFound.dto.UpdateUserRequestDTO;
import com.jsp.lostAndFound.dto.UserDTO;
import com.jsp.lostAndFound.entity.User;
import com.jsp.lostAndFound.exception.EmailAlreadyExistsException;
import com.jsp.lostAndFound.exception.UserNotFoundException;
import com.jsp.lostAndFound.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;

    public UserServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SecurityUtil securityUtil) {

this.userRepository = userRepository;
this.passwordEncoder = passwordEncoder;
this.securityUtil = securityUtil;
}

    @Override
    public UserDTO createUser(RegisterRequestDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "User with email " + dto.getEmail() + " already exists"
            );
        }

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        // Hash password before storing it in database
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setPhone(dto.getPhone());

        // Every newly registered user gets USER role
        user.setRole("USER");

        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return convertToDTO(savedUser);
    }

    @Override
    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with id " + id + " not found"
                        )
                );

        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UpdateUserRequestDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with id " + id + " not found"
                        )
                );

        // If email is changed, make sure the new email is not already used
        if (!user.getEmail().equalsIgnoreCase(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {

            throw new EmailAlreadyExistsException(
                    "User with email " + dto.getEmail() + " already exists"
            );
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        User updatedUser = userRepository.save(user);

        return convertToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(
                    "User with id " + id + " not found"
            );
        }

        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());

        return dto;
    
    }
    
    @Override
    public UserDTO getCurrentUser() {

        String email = securityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "Authenticated user not found"
                        )
                );

        return convertToDTO(user);
    }
}