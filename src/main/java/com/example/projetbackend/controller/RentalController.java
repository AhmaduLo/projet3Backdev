package com.example.projetbackend.controller;


import com.example.projetbackend.model.Rental;
import com.example.projetbackend.modelDTO.RentalDTO;
import com.example.projetbackend.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/rental/{userId}")
    public ResponseEntity<Rental> createRental(
            @PathVariable Integer userId,
            @RequestBody @Valid RentalDTO rentalDTO) {

        Rental createdRental = rentalService.createRental(userId, rentalDTO);
        return ResponseEntity
                .created(URI.create("/api/rental/" + createdRental.getId()))
                .body(createdRental);
    }
}
