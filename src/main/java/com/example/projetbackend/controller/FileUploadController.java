package com.example.projetbackend.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api")
public class FileUploadController {

    @PostMapping("/upload-file")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename(); // Crée un nom unique pour le fichier
        Path uploadPath = Paths.get("uploads"); // Le dossier dans lequel le fichier sera enregistré
        Files.createDirectories(uploadPath); // Crée le dossier s’il n'existe pas

        // Copie du fichier dans le dossier spécifié
        Files.copy(file.getInputStream(), uploadPath.resolve(filename));
        return ResponseEntity.ok("/uploads/" + filename);
    }

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<FileSystemResource> getFile(@PathVariable String filename) {
        File file = new File(System.getProperty("user.dir") + "/uploads/" + filename);
        if (file.exists()) {
            return ResponseEntity.ok(new FileSystemResource(file));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}