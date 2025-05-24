package com.example.fotballl_backend.controller;

import com.example.fotballl_backend.dto.SignUpRequest;
import com.example.fotballl_backend.jwt.JwtUtil;
import com.example.fotballl_backend.model.Role;
import com.example.fotballl_backend.model.User;
import com.example.fotballl_backend.repository.RoleRepository;
import com.example.fotballl_backend.repository.UserRepository;
import com.example.fotballl_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser( @RequestBody SignUpRequest signUpRequest) {
        try {
            User newUser = userService.registerUser(signUpRequest);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody SignUpRequest signUpRequest) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(signUpRequest.getUsername());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(signUpRequest.getPassword());  // Si vous voulez que l'admin puisse changer le mot de passe
            return ResponseEntity.ok(userRepository.save(user));
        }
        return ResponseEntity.status(404).body("User not found");
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return ResponseEntity.ok("User deleted");
        }
        return ResponseEntity.status(404).body("User not found");
    }



    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAllByRoles(Set.of(roleRepository.findByName("ROLE_USER").get())));
    }
}