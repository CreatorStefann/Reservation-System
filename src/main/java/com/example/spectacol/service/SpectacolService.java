package com.example.spectacol.service;

import com.example.spectacol.dto.CreateSpectacolRequest;
import com.example.spectacol.dto.UpdateSpectacolRequest;
import com.example.spectacol.model.Sala;
import com.example.spectacol.model.Spectacol;
import com.example.spectacol.repository.SalaRepository;
import com.example.spectacol.repository.SpectacolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpectacolService {

    private final SpectacolRepository spectacolRepository;
    private final SalaRepository salaRepository;

    public SpectacolService(SpectacolRepository spectacolRepository,
                            SalaRepository salaRepository) {
        this.spectacolRepository = spectacolRepository;
        this.salaRepository = salaRepository;
    }

    public Spectacol createSpectacol(CreateSpectacolRequest request) {

        Sala sala = salaRepository.findById(request.getSalaId())
                .orElseThrow(() -> new RuntimeException("Sala not found"));

        Spectacol spectacol = new Spectacol();
        spectacol.setTitle(request.getTitle());
        spectacol.setDescription(request.getDescription());
        spectacol.setDateTime(request.getDateTime());
        spectacol.setPrice(request.getPrice());
        spectacol.setSala(sala);

        return spectacolRepository.save(spectacol);
    }

    public List<Spectacol> getAllSpectacole() {
        return spectacolRepository.findAll();
    }

    public Spectacol getSpectacolById(Long id) {
        return spectacolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spectacol not found"));
    }

    public Spectacol updateSpectacol(Long id, UpdateSpectacolRequest request) {
        Spectacol spectacol = getSpectacolById(id);

        spectacol.setTitle(request.getTitle());
        spectacol.setDescription(request.getDescription());
        spectacol.setDateTime(request.getDateTime());
        spectacol.setPrice(request.getPrice());

        return spectacolRepository.save(spectacol);
    }

    public void deleteSpectacol(Long id) {
        Spectacol spectacol = getSpectacolById(id);
        spectacolRepository.delete(spectacol);
    }
}


