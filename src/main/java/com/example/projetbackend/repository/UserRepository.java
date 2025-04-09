package com.example.projetbackend.repository;

import com.example.projetbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositorty extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
