package com.example.quickhack.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Failed to upload");
        }
    }
}
