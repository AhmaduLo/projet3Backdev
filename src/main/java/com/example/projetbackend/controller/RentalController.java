package com.example.projetbackend.controller;


import com.example.projetbackend.model.Rental;
import com.example.projetbackend.modelDTO.RentalDTO;
import com.example.projetbackend.modelDTO.RentalFormDTO;
import com.example.projetbackend.modelDTO.RentalResponseDTO;
import com.example.projetbackend.repository.UserRepository;
import com.example.projetbackend.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Rental Management", description = "Endpoints pour la gestion des locations immobilières")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class RentalController {

    private final RentalService rentalService;

    // Méthode pour créer un Rental avec une image
    @Operation(summary = "Créer une nouvelle location", description = "Crée une nouvelle location avec une image")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Location créée avec succès", content = @Content(schema = @Schema(implementation = Rental.class))), @ApiResponse(responseCode = "400", description = "Données invalides"), @ApiResponse(responseCode = "401", description = "Non autorisé"), @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")})
    @PostMapping(value = "/rentals", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalResponseDTO> createRental( @Valid @ModelAttribute RentalFormDTO form, Authentication authentication) throws IOException {

        Integer userId = Integer.parseInt(authentication.getName());
        // Télécharge l'image et obtient l'URL
        String pictureUrl = rentalService.uploadImage(form.getPicture());

        //convertir RentalFormDTO en RentalDTO
        RentalDTO rentalDTO = new RentalDTO(form.getName(), form.getSurface(), form.getPrice(), pictureUrl, form.getDescription());

        // Crée la location et retourne la réponse
        Rental createdRental = rentalService.createRental(userId, rentalDTO);
        RentalResponseDTO responseDTO = new RentalResponseDTO(createdRental);
        responseDTO.setMessage("Rental created !");
        return ResponseEntity.created(URI.create("/api/rental/" + createdRental.getId())) // Renvoie la location créée avec son URI
                .body(responseDTO);
    }


    // Méthode pour uploader l'image (exemple)
    @Operation(summary = "Télécharger une image", description = "Télécharge une image et retourne son URL")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Image téléchargée avec succès", content = @Content(schema = @Schema(implementation = String.class))), @ApiResponse(responseCode = "400", description = "Fichier invalide"), @ApiResponse(responseCode = "401", description = "Non autorisé")})
    @PostMapping("/uploads")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String pictureUrl = rentalService.uploadImage(file);
        return ResponseEntity.ok(pictureUrl);
    }

    //---------Methode pour le update--------------
    @Operation(summary = "Mettre à jour une location", description = "Met à jour les informations d'une location existante")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Location mise à jour avec succès", content = @Content(schema = @Schema(implementation = RentalResponseDTO.class))), @ApiResponse(responseCode = "400", description = "Données invalides"), @ApiResponse(responseCode = "401", description = "Non autorisé"), @ApiResponse(responseCode = "404", description = "Location non trouvée")})
    @PutMapping(value = "/rentals/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalResponseDTO> updateRental(@PathVariable Integer id, @Valid @ModelAttribute RentalFormDTO form, Authentication authentication) throws IOException {
        // Récupère l'ID de l'utilisateur authentifié
        Integer userId = Integer.parseInt(authentication.getName());

        // Vérifie si une nouvelle image est fournie, sinon conserve l'image existante
        String pictureUrl = form.getPicture() != null && !form.getPicture().isEmpty() ? rentalService.uploadImage(form.getPicture()) : null;

        // Récupère l'objet Rental existant
        Rental existingRental = rentalService.getRentalById(id); // Cette méthode doit exister dans RentalService

        // Si une image est fournie, utilise la nouvelle image, sinon garde l'ancienne image
        String finalPicture = (pictureUrl != null) ? pictureUrl : existingRental.getPicture();

        // Construction du DTO avec les informations mises à jour
        RentalDTO rentalDTO = new RentalDTO(form.getName(), form.getSurface(), form.getPrice(), finalPicture,  // Utilise finalPicture pour l'image
                form.getDescription());

        // Mise à jour du Rental
        Rental updatedRental = rentalService.updateRental(id, rentalDTO, userId);
        RentalResponseDTO response = new RentalResponseDTO(updatedRental);
        response.setMessage("Rental updated !");
        return ResponseEntity.ok(response);
    }

    //---------Methode pour getById--------------
    @Operation(summary = "Récupérer une location par ID", description = "Retourne les détails d'une location spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location trouvée", content = @Content(schema = @Schema(implementation = RentalResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Location non trouvée"),
            @ApiResponse(responseCode = "401", description = "Non autorisé")
    })
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalResponseDTO> getRentalById(@PathVariable Integer id) {
        Rental rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(new RentalResponseDTO(rental));
    }

    //---------Methode pour le getAll--------------
    @Operation(summary = "Récupérer toutes les locations", description = "Retourne la liste de toutes les locations disponibles")
    @ApiResponse(responseCode = "200", description = "Liste des locations récupérée avec succès", content = @Content(schema = @Schema(implementation = RentalResponseDTO.class)))
    @GetMapping("/rentals")
    public ResponseEntity<Map<String, Object>> getAllRentals() {
        List<RentalResponseDTO> rentals = rentalService.getAllRentals();
        Map<String, Object> response = new HashMap<>();
        response.put("rentals", rentals);
        return ResponseEntity.ok(response);
    }

    //---------Methode pour le delete--------------
    @Operation(summary = "Supprimer une location", description = "Supprime une location spécifique")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Location supprimée avec succès"), @ApiResponse(responseCode = "401", description = "Non autorisé"), @ApiResponse(responseCode = "404", description = "Location non trouvée")})
    @DeleteMapping("/rentals/{id}")
    public ResponseEntity<String> deleteRental(@PathVariable Integer id, Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        rentalService.deleteRental(id, userId);
        return ResponseEntity.ok("La location a été supprimée avec succès.");
    }

    //---------Methode pour le delete tous ces location--------------
    @Operation(summary = "Supprimer toutes les locations d'un utilisateur", description = "Supprime toutes les locations appartenant à l'utilisateur connecté")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Toutes les locations ont été supprimées"), @ApiResponse(responseCode = "401", description = "Non autorisé")})
    @DeleteMapping("/rentals")
    public ResponseEntity<String> deleteAllUserRentals(Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        rentalService.deleteAllRentalsByUserId(userId);
        return ResponseEntity.ok("Toutes vos locations ont été supprimées avec succès.");
    }

}
