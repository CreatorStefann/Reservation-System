package com.example.spectacol.service;

import com.example.spectacol.model.*;
import com.example.spectacol.model.enums.ReservationStatus;
import com.example.spectacol.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RezervareServiceTest {

    @InjectMocks
    private RezervareService rezervareService;

    @Mock private RezervareRepository rezervareRepository;
    @Mock private RezervareLocRepository rezervareLocRepository;
    @Mock private ClientRepository clientRepository;
    @Mock private SpectacolRepository spectacolRepository;
    @Mock private LocRepository locRepository;

    private Client client;
    private Spectacol spectacol;
    private Loc loc;
    private Sala sala;

    @BeforeEach
    void setUp() {

        client = new Client("Ion", "Popescu", "ion@test.com");
        client.setId(1L);

        sala = new Sala("Sala 1", 100);
        sala.setId(1L);

        spectacol = new Spectacol("Hamlet", "Desc",
                LocalDateTime.now().plusDays(1), 50.0, sala);
        spectacol.setId(1L);

        loc = new Loc(1, 1, sala);
        loc.setId(1L);
    }

    @Test
    void shouldCreateReservation() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(spectacolRepository.findById(1L)).thenReturn(Optional.of(spectacol));
        when(locRepository.findAllById(List.of(1L))).thenReturn(List.of(loc));
        when(rezervareLocRepository.findByLocId(1L)).thenReturn(List.of());
        when(rezervareRepository.save(any(Rezervare.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Rezervare rezervare = rezervareService.createReservation(1L, 1L, List.of(1L));

        assertNotNull(rezervare);
        assertEquals(50.0, rezervare.getTotalPrice());
        assertEquals(ReservationStatus.ACTIVE, rezervare.getStatus());
    }

    @Test
    void shouldThrowWhenCreateReservationAndSeatAlreadyReserved() {
        Rezervare existing = new Rezervare();
        existing.setStatus(ReservationStatus.ACTIVE);
        existing.setSpectacol(spectacol);

        RezervareLoc rezervareLoc = new RezervareLoc();
        rezervareLoc.setRezervare(existing);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(spectacolRepository.findById(1L)).thenReturn(Optional.of(spectacol));
        when(locRepository.findAllById(List.of(1L))).thenReturn(List.of(loc));
        when(rezervareLocRepository.findByLocId(1L)).thenReturn(List.of(rezervareLoc));

        assertThrows(RuntimeException.class, () ->
                rezervareService.createReservation(1L, 1L, List.of(1L))
        );
    }

    @Test
    void shouldGetAllReservations() {
        Rezervare rezervare = new Rezervare(client, spectacol,
                LocalDateTime.now(), ReservationStatus.ACTIVE, 50.0);

        when(rezervareRepository.findAll()).thenReturn(List.of(rezervare));

        List<Rezervare> result = rezervareService.getAllReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ReservationStatus.ACTIVE, result.get(0).getStatus());
    }

    @Test
    void shouldGetReservationById() {
        Rezervare rezervare = new Rezervare(client, spectacol,
                LocalDateTime.now(), ReservationStatus.ACTIVE, 50.0);

        when(rezervareRepository.findById(1L)).thenReturn(Optional.of(rezervare));

        Rezervare result = rezervareService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(ReservationStatus.ACTIVE, result.getStatus());
    }

    @Test
    void shouldThrowWhenReservationNotFound() {
        when(rezervareRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                rezervareService.getReservationById(99L)
        );
    }

    @Test
    void shouldCancelReservation() {
        Rezervare rezervare = new Rezervare(client, spectacol,
                LocalDateTime.now(), ReservationStatus.ACTIVE, 50.0);
        rezervare.setId(1L);

        when(rezervareRepository.findById(1L)).thenReturn(Optional.of(rezervare));
        when(rezervareRepository.save(any(Rezervare.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        rezervareService.cancelReservation(1L);

        assertEquals(ReservationStatus.CANCELED, rezervare.getStatus());
        verify(rezervareRepository, times(1)).save(rezervare);
    }

    @Test
    void shouldUpdateReservationSeats() {
        Loc loc2 = new Loc(1, 2, sala);
        loc2.setId(2L);

        Rezervare rezervare = new Rezervare(client, spectacol,
                LocalDateTime.now(), ReservationStatus.ACTIVE, 50.0);
        rezervare.setId(1L);
        rezervare.setRezervareLocuri(new ArrayList<>());

        when(rezervareRepository.findById(1L)).thenReturn(Optional.of(rezervare));
        when(locRepository.findAllById(List.of(2L))).thenReturn(List.of(loc2));
        when(rezervareLocRepository.findByLocId(2L)).thenReturn(List.of());
        when(rezervareRepository.save(any(Rezervare.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Rezervare updated = rezervareService.updateReservationSeats(1L, List.of(2L));

        assertNotNull(updated);
        assertEquals(50.0, updated.getTotalPrice());
        verify(rezervareLocRepository, times(1)).saveAll(any());
    }

    @Test
    void shouldThrowWhenUpdateSeatsOnCanceledReservation() {
        Rezervare rezervare = new Rezervare(client, spectacol,
                LocalDateTime.now(), ReservationStatus.CANCELED, 50.0);
        rezervare.setId(1L);

        when(rezervareRepository.findById(1L)).thenReturn(Optional.of(rezervare));

        assertThrows(RuntimeException.class, () ->
                rezervareService.updateReservationSeats(1L, List.of(2L))
        );
    }
}
