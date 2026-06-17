package com.example.spectacol.service;

import com.example.spectacol.dto.CreateSalaRequest;
import com.example.spectacol.model.Loc;
import com.example.spectacol.model.Sala;
import com.example.spectacol.repository.LocRepository;
import com.example.spectacol.repository.SalaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalaService {

    private final SalaRepository salaRepository;
    private final LocRepository locRepository;

    public SalaService(SalaRepository salaRepository,
                       LocRepository locRepository) {
        this.salaRepository = salaRepository;
        this.locRepository = locRepository;
    }

    @Transactional
    public Sala createSala(CreateSalaRequest request) {

        Sala sala = new Sala();
        sala.setName(request.getName());
        sala.setCapacity(request.getRows() * request.getSeatsPerRow());

        Sala savedSala = salaRepository.save(sala);

        List<Loc> locuri = new ArrayList<>();

        for (int i = 1; i <= request.getRows(); i++) {
            for (int j = 1; j <= request.getSeatsPerRow(); j++) {

                Loc loc = new Loc();
                loc.setRowNumber(i);
                loc.setSeatNumber(j);
                loc.setSala(savedSala);

                locuri.add(loc);
            }
        }

        locRepository.saveAll(locuri);

        return savedSala;
    }
}
