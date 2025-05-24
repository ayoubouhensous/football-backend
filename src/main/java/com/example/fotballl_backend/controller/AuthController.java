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
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
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
            User authenticatedUser = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
            String token = jwtUtil.generateToken(authenticatedUser.getEmail());

            // Vérifier si l'utilisateur a le rôle ADMIN
            boolean isAdmin = authenticatedUser.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

            return ResponseEntity.ok(new JwtResponse(token, authenticatedUser.getUsername(), isAdmin));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Erreur interne du serveur"));
        }
    }


}