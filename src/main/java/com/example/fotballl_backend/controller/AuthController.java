package com.example.fotballl_backend.controller;

import com.example.fotballl_backend.dto.LoginRequest;
import com.example.fotballl_backend.dto.SignUpRequest;
import com.example.fotballl_backend.jwt.ErrorResponse;
import com.example.fotballl_backend.jwt.JwtResponse;
import com.example.fotballl_backend.jwt.JwtUtil;
import com.example.fotballl_backend.model.User;
import com.example.fotballl_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    @Autowired
    private AuthService userService;
    @Autowired
    private JwtUtil jwtUtil;  // Assurez-vous d'ajouter l'injection du JwtUtil

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        try {
            User newUser = userService.registerUser(signUpRequest);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authentifier l'utilisateur
            User authenticatedUser = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            // Générer le token JWT
            String token = jwtUtil.generateToken(authenticatedUser.getEmail());

            // Retourner une réponse avec le token et le nom d'utilisateur
            return ResponseEntity.ok(new JwtResponse(token, authenticatedUser.getUsername()));
        } catch (RuntimeException e) {
            // Si une exception RuntimeException survient (email ou mot de passe incorrect)
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()) {
            });
        } catch (Exception e) {
            // Si une autre exception survient
            return ResponseEntity.status(500).body(new ErrorResponse("Erreur interne du serveur"));
        }
    }

}
