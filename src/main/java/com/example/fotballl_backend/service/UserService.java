package com.example.fotballl_backend.service;

import com.example.fotballl_backend.dto.SignUpRequest;
import com.example.fotballl_backend.model.Role;
import com.example.fotballl_backend.model.User;
import com.example.fotballl_backend.repository.RoleRepository;
import com.example.fotballl_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(SignUpRequest signUpRequest) throws Exception {
        // Vérification si l'email existe déjà
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new Exception("Email déjà utilisé");
        }

        // Création de l'utilisateur avec les rôles par défaut
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new Exception("Rôle USER introuvable"));

        // Encoder le mot de passe
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setRoles(new HashSet<>(Collections.singleton(userRole)));  // Assigner le rôle USER

        return userRepository.save(user); // Sauvegarder l'utilisateur dans la base de données
    }

    /**
     * Méthode pour authentifier un utilisateur
     */
    public User authenticateUser(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Mot de passe incorrect");
        }

        return user;
    }
}