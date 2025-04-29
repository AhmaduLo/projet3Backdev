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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Validated
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;


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
}
