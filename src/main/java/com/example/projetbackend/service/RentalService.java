package com.example.projetbackend.service;


import com.example.projetbackend.DTO.RentalDTO;
import com.example.projetbackend.DTO.RentalResponseDTO;
import com.example.projetbackend.ResourceNotFoundException;
import com.example.projetbackend.model.Rental;
import com.example.projetbackend.model.User;
import com.example.projetbackend.repository.RentalRepository;
import com.example.projetbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
@Builder
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RentalResponseDTO createRentalForUser(RentalDTO rentalDTO, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Rental rental = null;
        try {
            rental = Rental.builder()
                    .name(rentalDTO.getName())
                    .surface(rentalDTO.getSurface())
                    .price(rentalDTO.getPrice())
                    .picture(rentalDTO.getPicture().getBytes()) // byte[] ou String selon votre modèle
                    .description(rentalDTO.getDescription())
                    .owner(owner)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Rental savedRental = rentalRepository.save(rental);
        return convertToResponseDTO(savedRental);
    }
    //5 . Conversion Entity → DTO
    private RentalResponseDTO convertToResponseDTO(Rental savedRental) {
        return RentalResponseDTO.builder()
                .id(savedRental.getId() != null ? savedRental.getId().longValue() : null)
                .name(savedRental.getName())
                .surface(savedRental.getSurface())
                .price(savedRental.getPrice())
                .picture(savedRental.getPicture())
                .description(savedRental.getDescription())
                .createdAt(savedRental.getCreatedAt())
                .ownerId(savedRental.getOwner() != null ? savedRental.getOwner().getId().longValue() : null)
                .build();
    }
    //----get tout les renders
//    @Transactional()
//    public List<RentalDTO> getAllRentals() {
//        List<Rental> rentals = rentalRepository.findAll();
//        return rentals.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    //------update-----------
//    @Transactional
//    public RentalDTO updateRental(Integer id, RentalDTO rentalDTO) {
//        Rental rental = rentalRepository.findById(Long.valueOf(id))
//                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));
//
//        // Mise à jour des champs
//        rental.setName(rentalDTO.getName());
//        rental.setSurface(rentalDTO.getSurface());
//        rental.setPrice(rentalDTO.getPrice());
//        rental.setPicture(rentalDTO.getPicture()); // ou gestion binaire si modifié
//        rental.setDescription(rentalDTO.getDescription());
//
//        Rental updatedRental = rentalRepository.save(rental);
//        return convertToDTO(updatedRental);
//    }

    //---------delete--------
    @Transactional
    public void deleteRental(Integer id) {
        Rental rental = rentalRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found with id: " + id));
        rentalRepository.delete(rental);
    }

}


