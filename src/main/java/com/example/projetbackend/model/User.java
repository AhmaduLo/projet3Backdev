package com.example.projetbackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // Indique que cette classe est une entité JPA
@Table(name = "USERS",
        indexes = @Index(name = "USERS_index", columnList = "email", unique = true))

public class User {

    // Getters et Setters
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    @NotBlank(message = "L'email est requis")
    @Email(message = "Format d'email invalide")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Setter
    @Getter
    @NotBlank(message = "Le nom est requis")
    @Column(nullable = false, length = 255)
    private String name;

    @Setter
    @Getter
    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Column(nullable = false, length = 255)
    private String password;

    @Setter
    @Getter
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Setter
    @Getter
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rental> rentals = new ArrayList<>();


    public User() {
    }

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

}
