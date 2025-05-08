package com.example.projetbackend.controller;


import com.example.projetbackend.model.Message;
import com.example.projetbackend.modelDTO.MessageRequestDTO;
import com.example.projetbackend.modelDTO.MessageResponseDTO;
import com.example.projetbackend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Message Management", description = "Endpoints pour la gestion des messages entre utilisateurs")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;

    // ------Créer un message--------
    @Operation(summary = "Créer un nouveau message", description = "Crée un nouveau message associé à une location")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Message créé avec succès", content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))), @ApiResponse(responseCode = "400", description = "Données invalides"), @ApiResponse(responseCode = "401", description = "Non autorisé"), @ApiResponse(responseCode = "404", description = "Location non trouvée")})
    @PostMapping("/messages")
    public ResponseEntity<Map<String, String>> createMessage(@RequestBody MessageRequestDTO messageRequestDTO, Authentication authentication) {

        Integer userId = Integer.parseInt(authentication.getName());
        Integer rentalId = messageRequestDTO.getRental_id();
        String content = messageRequestDTO.getMessage();


        if (rentalId == null || content == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "RentalId and message must not be null"));
        }

        messageService.createMessage(userId, rentalId, content);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Message send with success");

        return ResponseEntity.ok(response);
    }

    // ------recupere message par Rental--------
    @Operation(summary = "Récupérer les messages d'une location", description = "Retourne tous les messages associés à une location spécifique")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Messages récupérés avec succès", content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Aucun message trouvé pour cette location")})
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<?> getMessagesByRental(@PathVariable Integer rentalId) {
        List<Message> messages = messageService.getMessagesByRental(rentalId);

        if (messages.isEmpty()) {
            return ResponseEntity.status(404).body("Il n'y a pas de messages pour cette location.");
        }

        List<MessageResponseDTO> response = messages.stream().map(MessageResponseDTO::new).toList();

        return ResponseEntity.ok(response);
    }

    // ---------recupere tous les messages d'un utilisateur--------
    @Operation(summary = "Récupérer les messages d'un utilisateur", description = "Retourne tous les messages envoyés/reçus par l'utilisateur connecté")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Messages récupérés avec succès", content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Aucun message trouvé pour cet utilisateur")})
    @GetMapping("/user")
    public ResponseEntity<?> getMessagesByUser(Authentication authentication) {
        Integer userId = Integer.parseInt(authentication.getName());
        List<MessageResponseDTO> messages = messageService.getMessagesByUser(userId).stream().map(MessageResponseDTO::new).toList();

        if (messages.isEmpty()) {
            return ResponseEntity.status(404).body("Il n'y a pas de messages pour cet utilisateur.");
        }

        return ResponseEntity.ok(messages);
    }

    // ---------supprimer un message par messageID--------
    @Operation(summary = "Supprimer un message", description = "Supprime un message spécifique par son ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Message supprimé avec succès"), @ApiResponse(responseCode = "401", description = "Non autorisé"), @ApiResponse(responseCode = "404", description = "Message non trouvé")})
    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Integer messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok("Message supprimé.");
    }


}
