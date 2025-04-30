package com.example.projetbackend.repository;

import com.example.projetbackend.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    // Trouver un rental par son ID (équivalent à findById qui existe déjà)
    Optional<Rental> findById(Integer id);

    // Trouver tous les rentals d'un propriétaire
    List<Rental> findByOwnerId(Integer ownerId);
}
