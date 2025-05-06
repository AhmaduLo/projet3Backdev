package com.example.projetbackend.controller;

import com.example.projetbackend.UserAlreadyExistsException;
import com.example.projetbackend.model.User;
import com.example.projetbackend.modelDTO.UserResponseDTO;
import com.example.projetbackend.security.JwtUtils;
import com.example.projetbackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name = "User Authentication", description = "Endpoints pour l'inscription et la connexion")
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @Operation(summary = "Enregistrement utilisateur", description = "Créer un nouveau compte utilisateur")
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(createdUser));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Connexion utilisateur", description = "Authentification et obtention d'un JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> authenticatedUser = userService.authenticateUser(user.getEmail(), user.getPassword());
        if (!authenticatedUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou mot de passe incorrect");
        }
        // 3. Génération du JWT si authentification réussie
        user = authenticatedUser.get();
        String token = jwtUtils.generateToken(user.getId().longValue(), user.getEmail());

        // 4. Construction de la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user",new UserResponseDTO(user)); // Optionnel: renvoyer les infos utilisateur

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Validation JWT", description = "Vérifie la validité d'un token JWT")
    @SecurityRequirement(name = "bearerAuth")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        if (jwtUtils.validateToken(token)) {
            return ResponseEntity.ok("Token est valide ✅");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ❌");
        }
    }
}
