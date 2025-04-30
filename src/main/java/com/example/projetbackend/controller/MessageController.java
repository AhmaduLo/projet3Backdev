package com.example.projetbackend.controller;


import com.example.projetbackend.model.Message;
import com.example.projetbackend.modelDTO.MessageResponseDTO;
import com.example.projetbackend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    // ------Créer un message--------
    @PostMapping("/rental/{rentalId}")
    public ResponseEntity<MessageResponseDTO> createMessage(
            @PathVariable Integer rentalId,
            @RequestParam("message") String content,
            Authentication authentication) {

        Integer userId = Integer.parseInt(authentication.getName());
        Message message = messageService.createMessage(userId, rentalId, content);
        return ResponseEntity.ok(new MessageResponseDTO(message));
    }

    // ------recupere message par Rental--------
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<?> getMessagesByRental(@PathVariable Integer rentalId) {
        List<Message> messages = messageService.getMessagesByRental(rentalId);

        if (messages.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body("Il n'y a pas de messages pour cette location.");
        }

        List<MessageResponseDTO> response = messages.stream()
                .map(MessageResponseDTO::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    // ---------recupere tous les messages d'un utilisateur--------
    @GetMapping("/user")
    public ResponseEntity<?> getMessagesByUser(Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        List<MessageResponseDTO> messages = messageService.getMessagesByUser(userId)
                .stream()
                .map(MessageResponseDTO::new)
                .toList();

        if (messages.isEmpty()) {
            return ResponseEntity.status(404).body("Il n'y a pas de messages pour cet utilisateur.");
        }

        return ResponseEntity.ok(messages);
    }

    // ---------supprimer un message par messageID--------
    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Integer messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok("Message supprimé.");
    }


}
