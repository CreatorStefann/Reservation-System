package com.example.spectacol.service;

import com.example.spectacol.model.*;
import com.example.spectacol.model.enums.ReservationStatus;
import com.example.spectacol.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RezervareService {

    private final RezervareRepository rezervareRepository;
    private final RezervareLocRepository rezervareLocRepository;
    private final ClientRepository clientRepository;
    private final SpectacolRepository spectacolRepository;
    private final LocRepository locRepository;

    public RezervareService(RezervareRepository rezervareRepository,
                            RezervareLocRepository rezervareLocRepository,
                            ClientRepository clientRepository,
                            SpectacolRepository spectacolRepository,
                            LocRepository locRepository) {
        this.rezervareRepository = rezervareRepository;
        this.rezervareLocRepository = rezervareLocRepository;
        this.clientRepository = clientRepository;
        this.spectacolRepository = spectacolRepository;
        this.locRepository = locRepository;
    }

    @Transactional
    public Rezervare createReservation(Long clientId,
                                       Long spectacolId,
                                       List<Long> locIds) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Spectacol spectacol = spectacolRepository.findById(spectacolId)
                .orElseThrow(() -> new RuntimeException("Spectacol not found"));

        List<Loc> locuri = locRepository.findAllById(locIds);

        if (locuri.size() != locIds.size()) {
            throw new RuntimeException("One or more seats not found");
        }

        // verificare disponib locuri
        for (Long locId : locIds) {
            List<RezervareLoc> rezervariExistente =
                    rezervareLocRepository.findByLocId(locId);

            for (RezervareLoc rl : rezervariExistente) {
                if (rl.getRezervare().getSpectacol().getId().equals(spectacolId)
                        && rl.getRezervare().getStatus() == ReservationStatus.ACTIVE) {
                    throw new RuntimeException("Seat already reserved for this show");
                }
            }
        }

        Double totalPrice = spectacol.getPrice() * locIds.size();

        Rezervare rezervare = new Rezervare();
        rezervare.setClient(client);
        rezervare.setSpectacol(spectacol);
        rezervare.setReservationDate(LocalDateTime.now());
        rezervare.setStatus(ReservationStatus.ACTIVE);
        rezervare.setTotalPrice(totalPrice);

        Rezervare savedRezervare = rezervareRepository.save(rezervare);

        List<RezervareLoc> rezervareLocuri = new ArrayList<>();

        for (Loc loc : locuri) {
            RezervareLoc rezervareLoc = new RezervareLoc();
            rezervareLoc.setRezervare(savedRezervare);
            rezervareLoc.setLoc(loc);
            rezervareLocuri.add(rezervareLoc);
        }

        rezervareLocRepository.saveAll(rezervareLocuri);

        return savedRezervare;
    }

    public List<Rezervare> getAllReservations() {
        return rezervareRepository.findAll();
    }

    public Rezervare getReservationById(Long id) {
        return rezervareRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    @Transactional
    public void cancelReservation(Long rezervareId) {

        Rezervare rezervare = getReservationById(rezervareId);
        rezervare.setStatus(ReservationStatus.CANCELED);
        rezervareRepository.save(rezervare);
    }

    public List<Loc> getAvailableSeats(Long spectacolId) {

        Spectacol spectacol = spectacolRepository.findById(spectacolId)
                .orElseThrow(() -> new RuntimeException("Spectacol not found"));

        List<Loc> toateLocurile = locRepository.findBySalaId(
                spectacol.getSala().getId()
        );

        List<Rezervare> rezervari = rezervareRepository.findAll();

        List<Long> locuriOcupate = new ArrayList<>();

        for (Rezervare rezervare : rezervari) {
            if (rezervare.getSpectacol().getId().equals(spectacolId)
                    && rezervare.getStatus() == ReservationStatus.ACTIVE) {

                for (RezervareLoc rl : rezervare.getRezervareLocuri()) {
                    locuriOcupate.add(rl.getLoc().getId());
                }
            }
        }

        return toateLocurile.stream()
                .filter(loc -> !locuriOcupate.contains(loc.getId()))
                .toList();
    }

    @Transactional
    public Rezervare updateReservationSeats(Long rezervareId, List<Long> newLocIds) {
        Rezervare rezervare = getReservationById(rezervareId);

        if (rezervare.getStatus() != ReservationStatus.ACTIVE) {
            throw new RuntimeException("Cannot update a canceled reservation");
        }

        // Remove old seats
        List<RezervareLoc> existingSeats = new ArrayList<>(rezervare.getRezervareLocuri());
        for (RezervareLoc rl : existingSeats) {
            rezervareLocRepository.delete(rl);
        }

        // Validate new seats
        List<Loc> newLocuri = locRepository.findAllById(newLocIds);
        if (newLocuri.size() != newLocIds.size()) {
            throw new RuntimeException("One or more seats not found");
        }

        // Check availability of new seats
        for (Long locId : newLocIds) {
            List<RezervareLoc> rezervariExistente = rezervareLocRepository.findByLocId(locId);

            for (RezervareLoc rl : rezervariExistente) {
                if (rl.getRezervare().getSpectacol().getId().equals(rezervare.getSpectacol().getId())
                        && rl.getRezervare().getStatus() == ReservationStatus.ACTIVE
                        && !rl.getRezervare().getId().equals(rezervareId)) {
                    throw new RuntimeException("One or more seats are already reserved");
                }
            }
        }

        // Update total price
        Double newTotalPrice = rezervare.getSpectacol().getPrice() * newLocIds.size();
        rezervare.setTotalPrice(newTotalPrice);

        // Add new seats
        List<RezervareLoc> novaRezervaraLocuri = new ArrayList<>();
        for (Loc loc : newLocuri) {
            RezervareLoc rezervareLoc = new RezervareLoc();
            rezervareLoc.setRezervare(rezervare);
            rezervareLoc.setLoc(loc);
            novaRezervaraLocuri.add(rezervareLoc);
        }

        rezervareLocRepository.saveAll(novaRezervaraLocuri);
        return rezervareRepository.save(rezervare);
    }
}

