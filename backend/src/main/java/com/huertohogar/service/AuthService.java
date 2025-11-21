package com.huertohogar.service;

import com.huertohogar.model.dto.AuthRequest;
import com.huertohogar.model.dto.AuthResponse;
import com.huertohogar.model.dto.RegisterRequest;
import com.huertohogar.model.dto.UserDTO;
import com.huertohogar.model.entity.User;
import com.huertohogar.repository.UserRepository;
import com.huertohogar.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setLoyaltyPoints(0);
        user.setIsActive(true);
        
        User saved = userRepository.save(user);
        
        String token = jwtUtil.generateToken(saved.getEmail());
        UserDTO userDTO = toUserDTO(saved);
        
        return new AuthResponse(token, userDTO);
    }
    
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        
        if (!user.getIsActive()) {
            throw new RuntimeException("Usuario inactivo");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        String token = jwtUtil.generateToken(user.getEmail());
        UserDTO userDTO = toUserDTO(user);
        
        return new AuthResponse(token, userDTO);
    }
    
    private UserDTO toUserDTO(User user) {
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
