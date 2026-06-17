package com.example.spectacol.controller;

import com.example.spectacol.dto.CreateReservationRequest;
import com.example.spectacol.exception.GlobalExceptionHandler;
import com.example.spectacol.model.*;
import com.example.spectacol.model.enums.ReservationStatus;
import com.example.spectacol.service.RezervareService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RezervareController.class)
@Import(GlobalExceptionHandler.class)
class RezervareControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private RezervareService rezervareService;

    private Rezervare rezervare;
    private Sala sala;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        sala = new Sala("Sala 1", 100);
        sala.setId(1L);

        Spectacol spectacol = new Spectacol("Hamlet", "Desc",
                LocalDateTime.now().plusDays(1), 50.0, sala);
        spectacol.setId(1L);

        Client client = new Client("Ion", "Popescu", "ion@test.com");
        client.setId(1L);

        rezervare = new Rezervare();
        rezervare.setId(1L);
        rezervare.setClient(client);
        rezervare.setSpectacol(spectacol);
        rezervare.setStatus(ReservationStatus.ACTIVE);
        rezervare.setTotalPrice(100.0);
        rezervare.setReservationDate(LocalDateTime.now());
    }

    @Test
    void shouldCreateReservation() throws Exception {
        CreateReservationRequest req = new CreateReservationRequest();
        req.setClientId(1L);
        req.setSpectacolId(1L);
        req.setLocIds(List.of(1L, 2L));

        when(rezervareService.createReservation(1L, 1L, List.of(1L, 2L)))
                .thenReturn(rezervare);

        mockMvc.perform(post("/api/rezervari")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(100.0))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn400WhenCreateReservationWithNoSeats() throws Exception {
        CreateReservationRequest req = new CreateReservationRequest();
        req.setClientId(1L);
        req.setSpectacolId(1L);
        req.setLocIds(List.of());

        mockMvc.perform(post("/api/rezervari")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllReservations() throws Exception {
        when(rezervareService.getAllReservations()).thenReturn(List.of(rezervare));

        mockMvc.perform(get("/api/rezervari"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void shouldGetReservationById() throws Exception {
        when(rezervareService.getReservationById(1L)).thenReturn(rezervare);

        mockMvc.perform(get("/api/rezervari/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalPrice").value(100.0));
    }

    @Test
    void shouldReturn400WhenReservationNotFound() throws Exception {
        when(rezervareService.getReservationById(99L))
                .thenThrow(new RuntimeException("Reservation not found"));

        mockMvc.perform(get("/api/rezervari/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Reservation not found"));
    }

    @Test
    void shouldCancelReservation() throws Exception {
        doNothing().when(rezervareService).cancelReservation(1L);

        mockMvc.perform(delete("/api/rezervari/1"))
                .andExpect(status().isNoContent());

        verify(rezervareService, times(1)).cancelReservation(1L);
    }

    @Test
    void shouldUpdateReservationSeats() throws Exception {
        rezervare.setTotalPrice(50.0);
        when(rezervareService.updateReservationSeats(eq(1L), any()))
                .thenReturn(rezervare);

        mockMvc.perform(put("/api/rezervari/1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(3L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(50.0));
    }

    @Test
    void shouldGetAvailableSeats() throws Exception {
        Loc loc1 = new Loc(1, 1, sala);
        Loc loc2 = new Loc(1, 2, sala);

        when(rezervareService.getAvailableSeats(1L)).thenReturn(List.of(loc1, loc2));

        mockMvc.perform(get("/api/rezervari/spectacol/1/locuri-disponibile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
