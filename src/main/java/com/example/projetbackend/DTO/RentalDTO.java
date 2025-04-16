package com.example.projetbackend.DTO;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {

    private Long id;  // Exclure de la validation pour les créations

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 255, message = "Le nom ne doit pas dépasser 255 caractères")
    private String name;

    @NotNull(message = "La surface est obligatoire")
    @Positive(message = "La surface doit être positive")
    @Digits(integer = 10, fraction = 2, message = "Surface invalide (max 2 décimales)")
    private BigDecimal surface;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être positif")
    @Digits(integer = 10, fraction = 2, message = "Prix invalide (max 2 décimales)")
    private BigDecimal price;

    private byte[] picture;

    @Size(max = 2000, message = "La description est trop longue")
    private String description;

    private LocalDateTime createdAt;  // Non modifiable par l'utilisateur

    private Long ownerId; // Important pour la relation
}