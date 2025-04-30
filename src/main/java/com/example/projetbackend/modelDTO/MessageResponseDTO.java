package com.example.projetbackend.modelDTO;

import com.example.projetbackend.model.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponseDTO {
    private Integer id;
    private String content;
    private Integer username; // ou userId
    private Integer rentalId;
    private String createdAt;

    public MessageResponseDTO(Message message) {
        this.id = message.getId();
        this.content = message.getMessage();
        this.username = message.getUser().getId();
        this.rentalId = message.getRental().getId();
        this.createdAt = message.getCreatedAt().toString();
    }

    // getters...
}