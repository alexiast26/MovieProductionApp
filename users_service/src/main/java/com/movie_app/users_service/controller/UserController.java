package com.movie_app.users_service.controller;

import com.movie_app.users_service.domain.User;
import com.movie_app.users_service.domain.UserRole;
import com.movie_app.users_service.domain.dto.*;
import com.movie_app.users_service.domain.security.JwtProvider;
import com.movie_app.users_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        userService.registerUser(request.getUsername(), request.getEmail(), request.getPhone(), request.getPassword(), request.getUserRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponseDTO("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            User user = userService.login(request.getUsername(), request.getPassword());
            String token = jwtProvider.generateJwtToken(user);
            return ResponseEntity.ok(new JwtResponseDTO(token));
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsersForReporting() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> dtoList = users.stream().map(u -> new UserDTO(u.getId(), u.getUsername(), u.getEmail(), u.getPhone(), u.getRole())).toList();
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MessageResponseDTO> updateUser(@PathVariable Integer id, @RequestBody UpdateUserRequestDTO request) {
        userService.updateUser(id, request.getEmail(), request.getPhone(), request.getUserRole());
        return ResponseEntity.ok(new MessageResponseDTO("User updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MessageResponseDTO> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponseDTO("User deleted successfully"));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUserRole(@PathVariable UserRole role) {
        List<User> users = userService.getUsersByRole(role);
        List<UserDTO> userDTOS = users.stream().map(user -> {
            return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getRole());
        }).toList();

        return ResponseEntity.ok(userDTOS);
    }
}
