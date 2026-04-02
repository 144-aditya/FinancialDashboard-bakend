package com.example.financedashboard.service;

import com.example.financedashboard.dto.UserDTO;
import com.example.financedashboard.entity.User;
import com.example.financedashboard.enums.UserStatus;
import com.example.financedashboard.exception.ResourceNotFoundException;
import com.example.financedashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAllByDeletedFalse(pageable)
                .map(this::mapToDTO);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setName(userDTO.getName());
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Transactional
    public void deactivateUser(Long id) {
        int updated = userRepository.updateUserStatus(id, UserStatus.INACTIVE);
        if (updated == 0) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    @Transactional
    public void activateUser(Long id) {
        int updated = userRepository.updateUserStatus(id, UserStatus.ACTIVE);
        if (updated == 0) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setDeleted(true);
        userRepository.save(user);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}