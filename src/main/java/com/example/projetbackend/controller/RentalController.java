package com.example.projetbackend.controller;

import com.example.projetbackend.DTO.RentalDTO;
import com.example.projetbackend.DTO.RentalResponseDTO;
import com.example.projetbackend.ResourceNotFoundException;
import com.example.projetbackend.model.Rental;
import com.example.projetbackend.repository.RentalRepository;
import com.example.projetbackend.repository.UserRepository;
import com.example.projetbackend.service.RentalService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public RentalController(RentalService rentalService, UserRepository userRepository, RentalRepository rentalRepository) {
        this.rentalService = rentalService;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
    }

    //post rental d'un user
    @PostMapping(value = "/rental/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalResponseDTO> createRentalWithImage(
            @PathVariable Long userId,
            @ModelAttribute @Valid RentalResponseDTO rentalRequest,
            BindingResult bindingResult
    ) throws IOException, MethodArgumentNotValidException {
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(null, bindingResult);
        }

        byte[] pictureBytes = rentalRequest.getPicture() != null ? rentalRequest.getPicture().getBytes() : null;

        Rental rental = Rental.builder()
                .name(rentalRequest.getName())
                .surface(rentalRequest.getSurface())
                .price(rentalRequest.getPrice())
                .picture(pictureBytes)
                .description(rentalRequest.getDescription())
                .owner(userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")))
                .build();

        Rental saved = rentalRepository.save(rental);
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalService.convertToResponseDTO(saved));
    }

    //-----get all render
    @GetMapping("/rentals")
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }

    //update un rental
    @PutMapping(value = "/rental/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalDTO> updateRentalWithImage(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam(value = "picture", required = false) MultipartFile picture
    ) throws IOException {
        MultipartFile pictureBytes = picture != null ? picture : null;

        RentalDTO dto = RentalDTO.builder()
                .name(name)
                .surface(surface)
                .price(price)
                .description(description)
                .picture(pictureBytes)
                .build();

        RentalDTO updated = rentalService.updateRental(id, dto);
        return ResponseEntity.ok(updated);
    }


    //delete un rental
    @DeleteMapping("/rental/{id}")
    public ResponseEntity<Void> deleteRental(@PathVariable Integer id) {
        rentalService.deleteRental(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
