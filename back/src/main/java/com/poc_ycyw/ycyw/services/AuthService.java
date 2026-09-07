package com.poc_ycyw.ycyw.services;

import com.poc_ycyw.ycyw.JWTService;
import com.poc_ycyw.ycyw.dto.RegisterRequestDTO;
import com.poc_ycyw.ycyw.dto.TokenResponseDTO;
import com.poc_ycyw.ycyw.models.Utilisateur;
import com.poc_ycyw.ycyw.repository.UtilisateurRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JWTService jwtService, UtilisateurRepository utilisateurRepository, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterRequestDTO dto) {
        if (utilisateurRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        Utilisateur user = new Utilisateur();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setName(dto.getName());
        utilisateurRepository.save(user);
    }

    public TokenResponseDTO authenticateUser(String login, String password) {
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(login, password);
        Authentication authentication = authenticationManager.authenticate(authRequest);
        String token = jwtService.generateToken(authentication);

        String role = utilisateurRepository.findByEmail(login).orElseThrow().getRole();
        return new TokenResponseDTO(token, role);
    }
}
