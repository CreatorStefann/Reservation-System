package com.example.spectacol.service;

import com.example.spectacol.dto.CreateSpectacolRequest;
import com.example.spectacol.model.Sala;
import com.example.spectacol.model.Spectacol;
import com.example.spectacol.repository.SalaRepository;
import com.example.spectacol.repository.SpectacolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpectacolServiceTest {

    @Mock
    private SpectacolRepository spectacolRepository;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SpectacolService spectacolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateSpectacol() {

        CreateSpectacolRequest request = new CreateSpectacolRequest();
        request.setTitle("Hamlet");
        request.setDescription("Desc");
        request.setDateTime(LocalDateTime.now().plusDays(1));
        request.setPrice(50.0);
        request.setSalaId(1L);

        Sala sala = new Sala("Sala 1", 100);

        when(salaRepository.findById(1L))
                .thenReturn(Optional.of(sala));

        when(spectacolRepository.save(any(Spectacol.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Spectacol result = spectacolService.createSpectacol(request);

        assertNotNull(result);
        assertEquals("Hamlet", result.getTitle());
        assertEquals(50.0, result.getPrice());
    }

    @Test
    void shouldThrowWhenCreateSpectacolAndSalaNotFound() {

        CreateSpectacolRequest request = new CreateSpectacolRequest();
        request.setSalaId(1L);

        when(salaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                spectacolService.createSpectacol(request)
        );
    }
}
