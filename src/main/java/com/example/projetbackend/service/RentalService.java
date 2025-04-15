package com.example.projetbackend.service;


import com.example.projetbackend.DTO.RentalDTO;
import com.example.projetbackend.ResourceNotFoundException;
import com.example.projetbackend.model.Rental;
import com.example.projetbackend.model.User;
import com.example.projetbackend.repository.RentalRepository;
import com.example.projetbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.stereotype.Service;



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
    public RentalDTO createRentalForUser(RentalDTO rentalDTO, Long userId) {
        // 1. Vérifier que l'utilisateur existe
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));



        // 2. Convertir DTO -> Entity avec l'owner
        Rental rental = Rental.builder()
                .name(rentalDTO.getName())
                .surface(rentalDTO.getSurface())
                .price(rentalDTO.getPrice())
                .picture(rentalDTO.getPicture())
                .description(rentalDTO.getDescription())
                .owner(owner)
                .build();

        // 3. Sauvegarder
        Rental savedRental = rentalRepository.save(rental);

        // 4. Retourner le DTO
        return convertToDTO(savedRental);
        
    }
    //5 . Conversion Entity → DTO
    private RentalDTO convertToDTO(Rental savedRental) {
        if (savedRental == null) {
            return null;
        }
        return RentalDTO.builder()
                .id(Long.valueOf(savedRental.getId()))
                .name(savedRental.getName())
                .surface(savedRental.getSurface())
                .price(savedRental.getPrice())
                .picture(savedRental.getPicture())
                .description(savedRental.getDescription())
                .createdAt(savedRental.getCreatedAt())
                .ownerId(Long.valueOf(savedRental.getOwner() != null ? savedRental.getOwner().getId() : null))
                .build();
    }


}


