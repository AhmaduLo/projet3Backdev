package com.example.projetbackend.modelDTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class RentalFormDTO {

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(max = 255, message = "Le nom ne peut pas dépasser 255 caractères")
    private String name;

    @NotNull(message = "La surface ne peut pas être nulle")
    @DecimalMin(value = "0.0", inclusive = false, message = "La surface doit être supérieure à 0")
    @Digits(integer = 10, fraction = 2, message = "La surface doit avoir au maximum 2 décimales")
    private Double surface;

    @NotNull(message = "Le prix ne peut pas être nul")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    @Digits(integer = 10, fraction = 2, message = "Le prix doit avoir au maximum 2 décimales")
    private Double price;

    @NotBlank(message = "La description ne peut pas être vide")
    private String description;

    @NotNull(message = "Le fichier image est requis")
    private MultipartFile picture;

    // Getters & Setters


}