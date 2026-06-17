package com.example.spectacol.controller;

import com.example.spectacol.dto.CreateReservationRequest;
import com.example.spectacol.model.Loc;
import com.example.spectacol.model.Rezervare;
import com.example.spectacol.service.RezervareService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rezervari")
public class RezervareController {

    private final RezervareService rezervareService;

    public RezervareController(RezervareService rezervareService) {
        this.rezervareService = rezervareService;
    }

    @PostMapping
    public ResponseEntity<Rezervare> createReservation(
            @Valid @RequestBody CreateReservationRequest request) {

        Rezervare rezervare = rezervareService.createReservation(
                request.getClientId(),
                request.getSpectacolId(),
                request.getLocIds()
        );

        return ResponseEntity.ok(rezervare);
    }

    @GetMapping
    public ResponseEntity<List<Rezervare>> getAllReservations() {
        return ResponseEntity.ok(rezervareService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rezervare> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(rezervareService.getReservationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        rezervareService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/seats")
    public ResponseEntity<Rezervare> updateReservationSeats(
            @PathVariable Long id,
            @RequestBody List<Long> newLocIds) {
        return ResponseEntity.ok(rezervareService.updateReservationSeats(id, newLocIds));
    }

    @GetMapping("/spectacol/{spectacolId}/locuri-disponibile")
    public ResponseEntity<List<Loc>> getAvailableSeats(
            @PathVariable Long spectacolId) {

        return ResponseEntity.ok(
                rezervareService.getAvailableSeats(spectacolId)
        );
    }

}


