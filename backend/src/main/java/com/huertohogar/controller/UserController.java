package com.huertohogar.controller;

import com.huertohogar.model.dto.ApiResponse;
import com.huertohogar.model.dto.UserDTO;
import com.huertohogar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para gestión de usuarios")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/{email}")
    @Operation(summary = "Obtener usuario por email")
    public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
    
    @GetMapping
    @Operation(summary = "Obtener todos los usuarios")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @PutMapping("/{email}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
            @PathVariable String email,
            @Valid @RequestBody UserDTO userDTO) {
        UserDTO updated = userService.updateUser(email, userDTO);
        return ResponseEntity.ok(ApiResponse.success("Usuario actualizado exitosamente", updated));
    }
    
    @DeleteMapping("/{email}")
    @Operation(summary = "Eliminar usuario (desactivar)")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.ok(ApiResponse.success("Usuario desactivado exitosamente", null));
    }
}
