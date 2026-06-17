package com.example.spectacol.service;

import com.example.spectacol.dto.CreateSalaRequest;
import com.example.spectacol.dto.UpdateSalaRequest;
import com.example.spectacol.model.Sala;
import com.example.spectacol.repository.LocRepository;
import com.example.spectacol.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private LocRepository locRepository;

    @InjectMocks
    private SalaService salaService;

    private Sala sala;

    @BeforeEach
    void setUp() {
        sala = new Sala("Sala Mare", 6);
        sala.setId(1L);
    }

    @Test
    void shouldCreateSala() {
        CreateSalaRequest request = new CreateSalaRequest();
        request.setName("Sala Mare");
        request.setRows(2);
        request.setSeatsPerRow(3);

        when(salaRepository.save(any(Sala.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Sala result = salaService.createSala(request);

        assertNotNull(result);
        assertEquals("Sala Mare", result.getName());
        assertEquals(6, result.getCapacity());
        verify(locRepository, times(1)).saveAll(any());
    }

    @Test
    void shouldGetAllSali() {
        when(salaRepository.findAll()).thenReturn(List.of(sala));

        List<Sala> result = salaService.getAllSali();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sala Mare", result.get(0).getName());
    }

    @Test
    void shouldGetSalaById() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));

        Sala result = salaService.getSalaById(1L);

        assertNotNull(result);
        assertEquals("Sala Mare", result.getName());
    }

    @Test
    void shouldThrowWhenSalaNotFound() {
        when(salaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                salaService.getSalaById(99L)
        );
    }

    @Test
    void shouldUpdateSala() {
        UpdateSalaRequest request = new UpdateSalaRequest();
        request.setName("Sala Mica");
        request.setCapacity(50);

        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(salaRepository.save(any(Sala.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Sala updated = salaService.updateSala(1L, request);

        assertNotNull(updated);
        assertEquals("Sala Mica", updated.getName());
        assertEquals(50, updated.getCapacity());
    }

    @Test
    void shouldThrowWhenUpdateSalaNotFound() {
        UpdateSalaRequest request = new UpdateSalaRequest();
        request.setName("Sala Mica");
        request.setCapacity(50);

        when(salaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                salaService.updateSala(99L, request)
        );
    }

    @Test
    void shouldDeleteSala() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        doNothing().when(salaRepository).delete(sala);

        salaService.deleteSala(1L);

        verify(salaRepository, times(1)).delete(sala);
    }

    @Test
    void shouldThrowWhenDeleteSalaNotFound() {
        when(salaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                salaService.deleteSala(99L)
        );
    }
}
