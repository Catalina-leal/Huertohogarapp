package com.huertohogar.service;

import com.huertohogar.model.dto.UserDTO;
import com.huertohogar.model.entity.User;
import com.huertohogar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
        return toDTO(user);
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public UserDTO createUser(UserDTO userDTO, String password) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(userDTO.getFullName());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setRegion(userDTO.getRegion());
        user.setLoyaltyPoints(userDTO.getLoyaltyPoints() != null ? userDTO.getLoyaltyPoints() : 0);
        user.setIsActive(true);
        
        User saved = userRepository.save(user);
        return toDTO(saved);
    }
    
    public UserDTO updateUser(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
        
        user.setFullName(userDTO.getFullName());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setRegion(userDTO.getRegion());
        if (userDTO.getLoyaltyPoints() != null) {
            user.setLoyaltyPoints(userDTO.getLoyaltyPoints());
        }
        
        User updated = userRepository.save(user);
        return toDTO(updated);
    }
    
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
        user.setIsActive(false);
        userRepository.save(user);
    }
    
    public void addLoyaltyPoints(String email, Integer points) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
        user.setLoyaltyPoints(user.getLoyaltyPoints() + points);
        userRepository.save(user);
    }
    
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setRegion(user.getRegion());
        dto.setLoyaltyPoints(user.getLoyaltyPoints());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
