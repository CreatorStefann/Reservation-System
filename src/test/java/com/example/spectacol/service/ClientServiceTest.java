package com.example.spectacol.service;

import com.example.spectacol.model.Client;
import com.example.spectacol.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new Client("Ion", "Popescu", "ion@test.com");
    }

    @Test
    void shouldCreateClient() {

        when(clientRepository.findByEmail(client.getEmail()))
                .thenReturn(Optional.empty());

        when(clientRepository.save(client))
                .thenReturn(client);

        Client saved = clientService.createClient(client);

        assertNotNull(saved);
        assertEquals("ion@test.com", saved.getEmail());
    }

    @Test
    void shouldThrowWhenCreateClientAndEmailAlreadyExists() {

        when(clientRepository.findByEmail(client.getEmail()))
                .thenReturn(Optional.of(client));

        assertThrows(RuntimeException.class, () ->
                clientService.createClient(client)
        );
    }
}
