package com.example.projetbackend.service;

import com.example.projetbackend.model.Rental;
import com.example.projetbackend.model.User;
import com.example.projetbackend.modelDTO.RentalDTO;
import com.example.projetbackend.modelDTO.RentalResponseDTO;
import com.example.projetbackend.repository.RentalRepository;
import com.example.projetbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Validated
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;


    // Méthode pour récupérer un Rental par son ID
    public Rental getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with id: " + id));
    }
    @Transactional
    // Méthode pour créer un Rental
    public Rental createRental(Integer userId, RentalDTO rentalDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Rental rental = Rental.builder()
                .name(rentalDTO.getName())
                .surface(rentalDTO.getSurface())
                .price(rentalDTO.getPrice())
                .picture(rentalDTO.getPicture())
                .description(rentalDTO.getDescription())
                .owner(owner)  // Lier l'utilisateur au Rental
                .build();

        return rentalRepository.save(rental);
    }

    // Méthode pour uploader l'image
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Fichier vide !");
        }

        // Générer un nom unique pour l'image
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();

        // Créer le dossier si nécessaire
        Path uploadPath = Paths.get("uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Sauvegarder le fichier dans le dossier uploads/
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        // Retourner l'URL relative de l'image
        return "/uploads/" + filename;
    }

    //--------------Methode pour update rental-------------------
    @Transactional
    public Rental updateRental(Integer rentalId, RentalDTO rentalDTO, Integer userId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with id: " + rentalId));

        // Vérifier que le user est bien le propriétaire
        if (!rental.getOwner().getId().equals(userId)) {
            throw new SecurityException("Vous n'êtes pas autorisé à modifier cette location.");
        }

        // Mettre à jour les champs modifiables
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setDescription(rentalDTO.getDescription());
        rental.setPicture(rentalDTO.getPicture());

        return rentalRepository.save(rental);
    }

    //---------Methode pour le getAll--------------
    @Transactional
    public List<RentalResponseDTO> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(RentalResponseDTO::new)
                .toList();
    }


    //---------Methode pour le delete--------------

    @Transactional
    public void deleteRental(Integer rentalId, Integer userId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with id: " + rentalId));

        if (!rental.getOwner().getId().equals(userId)) {
            throw new SecurityException("Vous n'êtes pas autorisé à supprimer cette location.");
        }

        rentalRepository.delete(rental);
    }

    //---------Methode pour le delete tous ces location--------------
    @Transactional
    public void deleteAllRentalsByUserId(Integer userId) {
        List<Rental> rentals = rentalRepository.findByOwnerId(userId);
        rentalRepository.deleteAll(rentals);
    }
}
