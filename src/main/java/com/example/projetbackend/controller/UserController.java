package com.example.projetbackend.controller;

import com.example.projetbackend.UserAlreadyExistsException;
import com.example.projetbackend.model.User;
import com.example.projetbackend.security.JwtUtils;
import com.example.projetbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> authenticatedUser = userService.authenticateUser(
                user.getEmail(),
                user.getPassword()
        );
        if (!authenticatedUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou mot de passe incorrect");
        }
        // 3. Génération du JWT si authentification réussie
        user = authenticatedUser.get();
        String token = jwtUtils.generateToken(user.getId().longValue(), user.getEmail());

        // 4. Construction de la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user); // Optionnel: renvoyer les infos utilisateur

        return ResponseEntity.ok(response);
    }
}
