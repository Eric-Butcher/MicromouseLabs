package com.micromouselab.mazes.controller;

import com.micromouselab.mazes.domain.AuthResponseDTO;
import com.micromouselab.mazes.domain.LoginDTO;
import com.micromouselab.mazes.domain.RegisterDTO;
import com.micromouselab.mazes.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid LoginDTO loginRequest){
        AuthResponseDTO authResponseDTO = authService.authenticateUser(
                loginRequest.username(),
                loginRequest.plaintextPassword()
        );
        return authResponseDTO;
    }

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterDTO registerRequest){
        authService.registerUser(registerRequest);
        return "User registered successfully.";
    }
}
