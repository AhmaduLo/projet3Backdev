package com.example.projetbackend.controller;


import com.example.projetbackend.model.Rental;
import com.example.projetbackend.modelDTO.RentalDTO;
import com.example.projetbackend.modelDTO.RentalFormDTO;
import com.example.projetbackend.repository.UserRepository;
import com.example.projetbackend.service.RentalService;

import jakarta.validation.Valid;
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
            @Valid @ModelAttribute RentalFormDTO form) throws IOException {

        // Récupérer l'ID de l'utilisateur authentifié à partir du SecurityContext
//        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Télécharge l'image et obtient l'URL
        String pictureUrl = rentalService.uploadImage(form.getPicture());

        RentalDTO rentalDTO = new RentalDTO(
                form.getName(), form.getSurface(), form.getPrice(), pictureUrl, form.getDescription());

        // Crée la location et retourne la réponse
        Rental createdRental = rentalService.createRental(userId, rentalDTO);
        return ResponseEntity
                .created(URI.create("/api/rental/" + createdRental.getId())) // Renvoie la location créée avec son URI
                .body(createdRental);
    }

    // Méthode pour uploader l'image (exemple)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String pictureUrl = rentalService.uploadImage(file);
        return ResponseEntity.ok(pictureUrl);
    }
}
