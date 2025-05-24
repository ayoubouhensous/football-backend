package com.example.fotballl_backend;

import com.example.fotballl_backend.model.Role;
import com.example.fotballl_backend.model.User;
import com.example.fotballl_backend.repository.RoleRepository;
import com.example.fotballl_backend.repository.UserRepository;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class FotballlBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FotballlBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));


            // Exemple d'utilisateur
            String password = "motdepasse";
            String encryptedPassword = passwordEncoder.encode(password);

            // Créer un utilisateur avec le mot de passe crypté
            User user = User.builder()
                    .username("testuser")
                    .email("test@example.com")
                    .password(encryptedPassword)
                    .roles(new HashSet<>(Collections.singleton(userRole))) // Assigner le rôle admin
                    .build();

            // Sauvegarder l'utilisateur dans la base de données
            userRepository.save(user);

            // Créer un utilisateur admin
            String passwordd = "adminpassword";
            String encryptedPasswordd = passwordEncoder.encode(passwordd);

            User adminUser = User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .password(encryptedPasswordd)
                    .roles(new HashSet<>(Collections.singleton(adminRole))) // Assigner le rôle admin
                    .build();

            userRepository.save(adminUser);
        };
    }

}