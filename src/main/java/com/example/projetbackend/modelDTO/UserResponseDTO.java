package com.example.projetbackend.modelDTO;

import com.example.projetbackend.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDTO {
    private Integer id;
    private String email;
    private String name;
    private LocalDateTime createdAt;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.createdAt = user.getCreatedAt();
    }
}
