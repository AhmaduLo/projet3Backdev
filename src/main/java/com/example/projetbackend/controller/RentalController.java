package com.example.projetbackend.controller;

import com.example.projetbackend.DTO.RentalDTO;
import com.example.projetbackend.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/rental/{userId}")
    public ResponseEntity<RentalDTO> createRentalForUser(
            @PathVariable Long userId,
            @Valid @RequestBody RentalDTO rentalDTO) {

        RentalDTO createdRental = rentalService.createRentalForUser(rentalDTO, userId);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }


}

