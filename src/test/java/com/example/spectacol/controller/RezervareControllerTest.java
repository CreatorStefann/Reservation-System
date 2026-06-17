package com.example.spectacol.controller;

import com.example.spectacol.dto.CreateReservationRequest;
import com.example.spectacol.model.*;
import com.example.spectacol.model.enums.ReservationStatus;
import com.example.spectacol.service.RezervareService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RezervareController.class)
class RezervareControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RezervareService rezervareService;

    private ObjectMapper objectMapper= new ObjectMapper();

    @Test
    void shouldCreateReservation() throws Exception {

        Sala sala = new Sala("Sala 1", 100);
        Spectacol spectacol = new Spectacol(
                "Hamlet",
                "Desc",
                LocalDateTime.now().plusDays(1),
                50.0,
                sala
        );

        Client client = new Client("Ion", "Popescu", "ion@test.com");

        Rezervare rezervare = new Rezervare();
        rezervare.setClient(client);
        rezervare.setSpectacol(spectacol);
        rezervare.setStatus(ReservationStatus.ACTIVE);
        rezervare.setTotalPrice(100.0);

        CreateReservationRequest request = new CreateReservationRequest();
        request.setClientId(1L);
        request.setSpectacolId(1L);
        request.setLocIds(List.of(1L, 2L));

        Mockito.when(rezervareService.createReservation(
                        1L, 1L, List.of(1L, 2L)))
                .thenReturn(rezervare);

        mockMvc.perform(post("/api/rezervari")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(100.0))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldCancelReservation() throws Exception {

        mockMvc.perform(delete("/api/rezervari/1"))
                .andExpect(status().isNoContent());
    }
}
