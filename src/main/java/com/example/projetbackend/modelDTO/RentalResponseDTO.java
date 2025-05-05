package com.example.projetbackend.modelDTO;

import com.example.projetbackend.model.Rental;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
    private String ownerEmail;

    public RentalResponseDTO(Rental rental) {
        this.id = rental.getId();
        this.name = rental.getName();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.picture = rental.getPicture();
        this.description = rental.getDescription();
        this.ownerEmail = rental.getOwner().getEmail();
    }
}
