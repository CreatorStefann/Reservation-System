package com.example.spectacol.service;

import com.example.spectacol.model.*;
import com.example.spectacol.model.enums.ReservationStatus;
import com.example.spectacol.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RezervareServiceTest {

    @InjectMocks
    private RezervareService rezervareService;

    @Mock
    private RezervareRepository rezervareRepository;
    @Mock
    private RezervareLocRepository rezervareLocRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private SpectacolRepository spectacolRepository;
    @Mock
    private LocRepository locRepository;

    private Client client;
    private Spectacol spectacol;
    private Loc loc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new Client("Ion", "Popescu", "ion@test.com");

        Sala sala = new Sala("Sala 1", 100);

        spectacol = new Spectacol("Hamlet", "Desc",
                LocalDateTime.now().plusDays(1),
                50.0,
                sala);

        loc = new Loc(1, 1, sala);
    }

    @Test
    void shouldCreateReservation() {

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        when(spectacolRepository.findById(1L))
                .thenReturn(Optional.of(spectacol));

        when(locRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(loc));

        when(rezervareLocRepository.findByLocId(1L))
                .thenReturn(List.of());

        when(rezervareRepository.save(any(Rezervare.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Rezervare rezervare = rezervareService
                .createReservation(1L, 1L, List.of(1L));

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

        when(clientRepository.findById(1L))
                .thenReturn(Optional.of(client));

        when(spectacolRepository.findById(1L))
                .thenReturn(Optional.of(spectacol));

        when(locRepository.findAllById(List.of(1L)))
                .thenReturn(List.of(loc));

        when(rezervareLocRepository.findByLocId(1L))
                .thenReturn(List.of(rezervareLoc));

        assertThrows(RuntimeException.class, () ->
                rezervareService.createReservation(1L, 1L, List.of(1L))
        );
    }
}
