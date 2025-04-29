package com.example.projetbackend.service;

import com.example.projetbackend.model.Rental;
import com.example.projetbackend.model.User;
import com.example.projetbackend.modelDTO.RentalDTO;
import com.example.projetbackend.repository.RentalRepository;
import com.example.projetbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@RequiredArgsConstructor
@Validated
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;


    @Transactional
    public Rental createRental(Integer userId, @Valid RentalDTO rentalDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Rental rental = Rental.builder()
                .name(rentalDTO.getName())  // Correction de la faute de frappe "rentalDTO"
                .surface(rentalDTO.getSurface())
                .price(rentalDTO.getPrice())
                .picture(rentalDTO.getPicture())
                .description(rentalDTO.getDescription())
                .owner(owner)
                .build();

        return rentalRepository.save(rental);
    }
}
