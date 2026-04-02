package com.example.financedashboard.controller;

import com.example.financedashboard.dto.AuthRequest;
import com.example.financedashboard.dto.AuthResponse;
import com.example.financedashboard.dto.UserDTO;
import com.example.financedashboard.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(authService.register(userDTO), HttpStatus.CREATED);
    }
}