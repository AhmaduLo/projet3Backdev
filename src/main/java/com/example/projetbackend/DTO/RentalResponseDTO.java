package com.example.projetbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponseDTO {

    private Long id;
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private MultipartFile picture;
    private String description;
    private LocalDateTime createdAt;
    private Long ownerId;
}
