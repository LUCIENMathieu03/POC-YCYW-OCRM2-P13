package com.poc_ycyw.ycyw.controller;

import com.poc_ycyw.ycyw.dto.LoginRequestDTO;
import com.poc_ycyw.ycyw.dto.RegisterRequestDTO;
import com.poc_ycyw.ycyw.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String index() {
        return "Bienvenue sur l’API de Your Car Your Way 👋🚘";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO dto) {
        try {
            return ResponseEntity.ok(authService.authenticateUser(dto.getEmail(), dto.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO dto) {
        try {
            authService.registerUser(dto);
            return ResponseEntity.ok(authService.authenticateUser(dto.getEmail(), dto.getPassword()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
