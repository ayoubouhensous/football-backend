package com.example.fotballl_backend.repository;

import com.example.fotballl_backend.model.Role;
import com.example.fotballl_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findAllByRoles(Set<Role> roles);

}
