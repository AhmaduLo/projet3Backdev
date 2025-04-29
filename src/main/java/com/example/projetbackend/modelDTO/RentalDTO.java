package com.example.projetbackend.modelDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {

    private Integer id;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Size(max = 255, message = "Le nom ne peut pas dépasser 255 caractères")
    private String name;

    @NotNull(message = "La surface ne peut pas être nulle")
    @DecimalMin(value = "0.0", inclusive = false, message = "La surface doit être supérieure à 0")
    @Digits(integer = 10, fraction = 2, message = "La surface doit avoir au maximum 2 décimales")
    private BigDecimal surface;

    @NotNull(message = "Le prix ne peut pas être nul")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    @Digits(integer = 10, fraction = 2, message = "Le prix doit avoir au maximum 2 décimales")
    private BigDecimal price;

    @Size(max = 255, message = "L'URL de l'image ne peut pas dépasser 255 caractères")
    private String picture;

    @Size(max = 2000, message = "La description ne peut pas dépasser 2000 caractères")
    private String description;


    // Constructeur
    public RentalDTO(String name, double surface, double price, String picture, String description) {
        this.name = name;
        this.surface = BigDecimal.valueOf(surface);
        this.price = BigDecimal.valueOf(price);
        this.picture = picture;
        this.description = description;
    }
}
