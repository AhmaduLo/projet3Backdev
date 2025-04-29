package com.example.projetbackend.controller;


import com.example.projetbackend.model.Rental;
import com.example.projetbackend.modelDTO.RentalDTO;
import com.example.projetbackend.repository.UserRepository;
import com.example.projetbackend.service.RentalService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class RentalController {

    private final RentalService rentalService;
    private final UserRepository userRepository;

    // Méthode pour créer un Rental avec une image
    @PostMapping(value = "/rental/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Rental> createRental(
            @PathVariable Integer userId,
            @RequestParam("name") String name,
            @RequestParam("surface") Double surface,
            @RequestParam("price") Double price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile file) throws IOException {

        String pictureUrl = rentalService.uploadImage(file);

        RentalDTO rentalDTO = new RentalDTO(name, surface, price, pictureUrl, description);

        Rental createdRental = rentalService.createRental(userId, rentalDTO);
        return ResponseEntity
                .created(URI.create("/api/rental/" + createdRental.getId()))
                .body(createdRental);
    }

    // Méthode pour uploader l'image (exemple)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String pictureUrl = rentalService.uploadImage(file);
        return ResponseEntity.ok(pictureUrl);
    }
}
