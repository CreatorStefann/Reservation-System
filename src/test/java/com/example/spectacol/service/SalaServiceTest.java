package com.example.spectacol.service;

import com.example.spectacol.dto.CreateSalaRequest;
import com.example.spectacol.model.Sala;
import com.example.spectacol.repository.LocRepository;
import com.example.spectacol.repository.SalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private LocRepository locRepository;

    @InjectMocks
    private SalaService salaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
}
