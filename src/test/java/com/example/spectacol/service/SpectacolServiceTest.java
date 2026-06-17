package com.example.spectacol.service;

import com.example.spectacol.dto.CreateSpectacolRequest;
import com.example.spectacol.dto.UpdateSpectacolRequest;
import com.example.spectacol.model.Sala;
import com.example.spectacol.model.Spectacol;
import com.example.spectacol.repository.SalaRepository;
import com.example.spectacol.repository.SpectacolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpectacolServiceTest {

    @Mock
    private SpectacolRepository spectacolRepository;

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SpectacolService spectacolService;

    private Sala sala;
    private Spectacol spectacol;

    @BeforeEach
    void setUp() {
        sala = new Sala("Sala 1", 100);
        sala.setId(1L);
        spectacol = new Spectacol("Hamlet", "Desc",
                LocalDateTime.now().plusDays(1), 50.0, sala);
        spectacol.setId(1L);
    }

    @Test
    void shouldCreateSpectacol() {
        CreateSpectacolRequest request = new CreateSpectacolRequest();
        request.setTitle("Hamlet");
        request.setDescription("Desc");
        request.setDateTime(LocalDateTime.now().plusDays(1));
        request.setPrice(50.0);
        request.setSalaId(1L);

        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
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

        when(salaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                spectacolService.createSpectacol(request)
        );
    }

    @Test
    void shouldGetAllSpectacole() {
        when(spectacolRepository.findAll()).thenReturn(List.of(spectacol));

        List<Spectacol> result = spectacolService.getAllSpectacole();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hamlet", result.get(0).getTitle());
    }

    @Test
    void shouldGetSpectacolById() {
        when(spectacolRepository.findById(1L)).thenReturn(Optional.of(spectacol));

        Spectacol result = spectacolService.getSpectacolById(1L);

        assertNotNull(result);
        assertEquals("Hamlet", result.getTitle());
    }

    @Test
    void shouldThrowWhenSpectacolNotFound() {
        when(spectacolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                spectacolService.getSpectacolById(99L)
        );
    }

    @Test
    void shouldUpdateSpectacol() {
        UpdateSpectacolRequest request = new UpdateSpectacolRequest();
        request.setTitle("Macbeth");
        request.setDescription("New desc");
        request.setDateTime(LocalDateTime.now().plusDays(5));
        request.setPrice(75.0);

        when(spectacolRepository.findById(1L)).thenReturn(Optional.of(spectacol));
        when(spectacolRepository.save(any(Spectacol.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Spectacol updated = spectacolService.updateSpectacol(1L, request);

        assertNotNull(updated);
        assertEquals("Macbeth", updated.getTitle());
        assertEquals(75.0, updated.getPrice());
        assertEquals("New desc", updated.getDescription());
    }

    @Test
    void shouldThrowWhenUpdateSpectacolNotFound() {
        UpdateSpectacolRequest request = new UpdateSpectacolRequest();
        request.setTitle("Macbeth");
        request.setDateTime(LocalDateTime.now().plusDays(5));
        request.setPrice(75.0);

        when(spectacolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                spectacolService.updateSpectacol(99L, request)
        );
    }

    @Test
    void shouldDeleteSpectacol() {
        when(spectacolRepository.findById(1L)).thenReturn(Optional.of(spectacol));
        doNothing().when(spectacolRepository).delete(spectacol);

        spectacolService.deleteSpectacol(1L);

        verify(spectacolRepository, times(1)).delete(spectacol);
    }

    @Test
    void shouldThrowWhenDeleteSpectacolNotFound() {
        when(spectacolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                spectacolService.deleteSpectacol(99L)
        );
    }
}
