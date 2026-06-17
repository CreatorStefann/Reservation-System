package com.example.spectacol.service;

import com.example.spectacol.dto.UpdateClientRequest;
import com.example.spectacol.model.Client;
import com.example.spectacol.repository.ClientRepository;
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
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client("Ion", "Popescu", "ion@test.com");
        client.setId(1L);
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

    @Test
    void shouldGetAllClients() {
        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<Client> result = clientService.getAllClients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ion@test.com", result.get(0).getEmail());
    }

    @Test
    void shouldGetClientById() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Client result = clientService.getClientById(1L);

        assertNotNull(result);
        assertEquals("ion@test.com", result.getEmail());
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                clientService.getClientById(99L)
        );
    }

    @Test
    void shouldUpdateClient() {
        UpdateClientRequest request = new UpdateClientRequest();
        request.setFirstName("Gheorghe");
        request.setLastName("Ionescu");
        request.setEmail("gheorghe@test.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findByEmail("gheorghe@test.com")).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Client updated = clientService.updateClient(1L, request);

        assertNotNull(updated);
        assertEquals("Gheorghe", updated.getFirstName());
        assertEquals("Ionescu", updated.getLastName());
        assertEquals("gheorghe@test.com", updated.getEmail());
    }

    @Test
    void shouldThrowWhenUpdateClientAndEmailTakenByOtherClient() {
        Client otherClient = new Client("Alt", "User", "gheorghe@test.com");
        otherClient.setId(2L);

        UpdateClientRequest request = new UpdateClientRequest();
        request.setFirstName("Ion");
        request.setLastName("Popescu");
        request.setEmail("gheorghe@test.com");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.findByEmail("gheorghe@test.com"))
                .thenReturn(Optional.of(otherClient));

        assertThrows(RuntimeException.class, () ->
                clientService.updateClient(1L, request)
        );
    }

    @Test
    void shouldDeleteClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client);

        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void shouldThrowWhenDeleteClientNotFound() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                clientService.deleteClient(99L)
        );
    }
}
