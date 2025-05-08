package com.example.projetbackend.modelDTO;

import com.example.projetbackend.model.Rental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalResponseDTO {
    private Integer id;
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String picture;
    private String description;
    Integer owner_id;
    LocalDateTime created_at;
    LocalDateTime updated_at;
    private String message;

    public RentalResponseDTO(Rental rental) {
        this.id = rental.getId();
        this.name = rental.getName();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.picture = rental.getPicture();
        this.description = rental.getDescription();
        this.owner_id = rental.getOwner().getId();
        this.created_at = rental.getCreatedAt();
        this.updated_at = rental.getUpdatedAt();
    }
}
