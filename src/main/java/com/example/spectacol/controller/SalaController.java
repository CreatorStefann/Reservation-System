package com.example.spectacol.controller;

import com.example.spectacol.dto.CreateSalaRequest;
import com.example.spectacol.model.Sala;
import com.example.spectacol.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sali")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @PostMapping
    public ResponseEntity<Sala> createSala(
            @Valid @RequestBody CreateSalaRequest request) {

        return ResponseEntity.ok(salaService.createSala(request));
    }
}
