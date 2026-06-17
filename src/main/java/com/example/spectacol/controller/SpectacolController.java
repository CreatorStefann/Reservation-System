package com.example.spectacol.controller;

import com.example.spectacol.dto.CreateSpectacolRequest;
import com.example.spectacol.model.Spectacol;
import com.example.spectacol.service.SpectacolService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spectacole")
public class SpectacolController {

    private final SpectacolService spectacolService;

    public SpectacolController(SpectacolService spectacolService) {
        this.spectacolService = spectacolService;
    }

    @PostMapping
    public ResponseEntity<Spectacol> createSpectacol(
            @Valid @RequestBody CreateSpectacolRequest request) {

        return ResponseEntity.ok(
                spectacolService.createSpectacol(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<Spectacol>> getAllSpectacole() {
        return ResponseEntity.ok(
                spectacolService.getAllSpectacole()
        );
    }
}
