package com.example.projetbackend.modelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDTO {
    private Integer rental_id;
    private Integer user_id;
    private String message;
}
