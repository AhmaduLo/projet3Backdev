package com.example.projetbackend.controller;


import com.example.projetbackend.model.Rental;
import com.example.projetbackend.modelDTO.RentalDTO;
import com.example.projetbackend.modelDTO.RentalFormDTO;
import com.example.projetbackend.modelDTO.RentalResponseDTO;
import com.example.projetbackend.repository.UserRepository;
import com.example.projetbackend.service.RentalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    //---------Methode pour le update--------------
    @PutMapping(value = "/updateRental/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalResponseDTO> updateRental(
            @PathVariable Integer id,
            @Valid @ModelAttribute RentalFormDTO form,
            Authentication authentication
    ) throws IOException {
        // Récupère l'ID de l'utilisateur authentifié
        Integer userId = Integer.parseInt(authentication.getName());

        // Vérifie si une nouvelle image est fournie, sinon conserve l'image existante
        String pictureUrl = form.getPicture() != null && !form.getPicture().isEmpty()
                ? rentalService.uploadImage(form.getPicture())
                : null;

        // Récupère l'objet Rental existant
        Rental existingRental = rentalService.getRentalById(id); // Cette méthode doit exister dans RentalService

        // Si une image est fournie, utilise la nouvelle image, sinon garde l'ancienne image
        String finalPicture = (pictureUrl != null) ? pictureUrl : existingRental.getPicture();

        // Construction du DTO avec les informations mises à jour
        RentalDTO rentalDTO = new RentalDTO(
                form.getName(),
                form.getSurface(),
                form.getPrice(),
                finalPicture,  // Utilise finalPicture pour l'image
                form.getDescription()
        );

        // Mise à jour du Rental
        Rental updatedRental = rentalService.updateRental(id, rentalDTO, userId);
        return ResponseEntity.ok(new RentalResponseDTO(updatedRental));
    }
}
