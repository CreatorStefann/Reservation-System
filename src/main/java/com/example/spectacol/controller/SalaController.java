package com.example.spectacol.controller;

import com.example.spectacol.dto.CreateSalaRequest;
import com.example.spectacol.dto.UpdateSalaRequest;
import com.example.spectacol.model.Sala;
import com.example.spectacol.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Sala>> getAllSali() {
        return ResponseEntity.ok(salaService.getAllSali());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala> getSalaById(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.getSalaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> updateSala(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSalaRequest request) {
        return ResponseEntity.ok(salaService.updateSala(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSala(@PathVariable Long id) {
        salaService.deleteSala(id);
        return ResponseEntity.noContent().build();
    }
}

