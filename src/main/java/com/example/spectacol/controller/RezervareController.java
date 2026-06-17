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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {

        rezervareService.cancelReservation(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/spectacol/{spectacolId}/locuri-disponibile")
    public ResponseEntity<List<Loc>> getAvailableSeats(
            @PathVariable Long spectacolId) {

        return ResponseEntity.ok(
                rezervareService.getAvailableSeats(spectacolId)
        );
    }

}
