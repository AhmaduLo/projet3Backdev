package com.example.projetbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
}