package com.example.spectacol.controller;

import com.example.spectacol.dto.UpdateClientRequest;
import com.example.spectacol.exception.GlobalExceptionHandler;
import com.example.spectacol.model.Client;
import com.example.spectacol.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
@Import(GlobalExceptionHandler.class)
class ClientControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        client = new Client("Ion", "Popescu", "ion@test.com");
        client.setId(1L);
    }

    @Test
    void shouldCreateClient() throws Exception {
        Client input = new Client("Ion", "Popescu", "ion@test.com");
        when(clientService.createClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ion@test.com"))
                .andExpect(jsonPath("$.firstName").value("Ion"));
    }

    @Test
    void shouldReturn400WhenCreateClientWithBlankFirstName() throws Exception {
        Client invalid = new Client("", "Popescu", "ion@test.com");

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenCreateClientWithInvalidEmail() throws Exception {
        Client invalid = new Client("Ion", "Popescu", "not-an-email");

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllClients() throws Exception {
        when(clientService.getAllClients()).thenReturn(List.of(client));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("ion@test.com"));
    }

    @Test
    void shouldGetClientById() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(client);

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ion"));
    }

    @Test
    void shouldReturn400WhenClientNotFound() throws Exception {
        when(clientService.getClientById(99L))
                .thenThrow(new RuntimeException("Client not found"));

        mockMvc.perform(get("/api/clients/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Client not found"));
    }

    @Test
    void shouldUpdateClient() throws Exception {
        UpdateClientRequest req = new UpdateClientRequest();
        req.setFirstName("Gheorghe");
        req.setLastName("Ionescu");
        req.setEmail("g@test.com");

        Client updated = new Client("Gheorghe", "Ionescu", "g@test.com");
        updated.setId(1L);

        when(clientService.updateClient(eq(1L), any(UpdateClientRequest.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Gheorghe"))
                .andExpect(jsonPath("$.email").value("g@test.com"));
    }

    @Test
    void shouldDeleteClient() throws Exception {
        doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());

        verify(clientService, times(1)).deleteClient(1L);
    }
}
