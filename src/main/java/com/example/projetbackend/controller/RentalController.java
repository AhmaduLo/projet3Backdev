package com.example.projetbackend.controller;

import com.example.projetbackend.DTO.RentalDTO;
import com.example.projetbackend.ResourceNotFoundException;
import com.example.projetbackend.model.Rental;
import com.example.projetbackend.service.RentalService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    //post rental d'un user
    @PostMapping(value = "/rental/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RentalDTO> createRentalWithImage(
            @PathVariable Long userId,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture
    ) throws IOException {

        RentalDTO rentalDTO = RentalDTO.builder()
                .name(name)
                .surface(surface)
                .price(price)
                .description(description)
                .picture(picture.getBytes())
                .build();

        RentalDTO createdRental = rentalService.createRentalForUser(rentalDTO, userId);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
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
        byte[] pictureBytes = picture != null ? picture.getBytes() : null;

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
