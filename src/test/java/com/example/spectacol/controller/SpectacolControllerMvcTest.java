package com.example.spectacol.controller;

import com.example.spectacol.dto.CreateSpectacolRequest;
import com.example.spectacol.dto.UpdateSpectacolRequest;
import com.example.spectacol.exception.GlobalExceptionHandler;
import com.example.spectacol.model.Sala;
import com.example.spectacol.model.Spectacol;
import com.example.spectacol.service.SpectacolService;
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

@WebMvcTest(controllers = SpectacolController.class)
@Import(GlobalExceptionHandler.class)
class SpectacolControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private SpectacolService spectacolService;

    private Spectacol spectacol;
    private Sala sala;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        sala = new Sala("Sala 1", 100);
        sala.setId(1L);
        spectacol = new Spectacol("Hamlet", "Descriere",
                LocalDateTime.now().plusDays(1), 50.0, sala);
        spectacol.setId(1L);
    }

    @Test
    void shouldCreateSpectacol() throws Exception {
        CreateSpectacolRequest req = new CreateSpectacolRequest();
        req.setTitle("Hamlet");
        req.setDescription("Descriere");
        req.setDateTime(LocalDateTime.now().plusDays(1));
        req.setPrice(50.0);
        req.setSalaId(1L);

        when(spectacolService.createSpectacol(any(CreateSpectacolRequest.class)))
                .thenReturn(spectacol);

        mockMvc.perform(post("/api/spectacole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Hamlet"))
                .andExpect(jsonPath("$.price").value(50.0));
    }

    @Test
    void shouldReturn400WhenCreateSpectacolWithBlankTitle() throws Exception {
        CreateSpectacolRequest req = new CreateSpectacolRequest();
        req.setTitle("");
        req.setDateTime(LocalDateTime.now().plusDays(1));
        req.setPrice(50.0);
        req.setSalaId(1L);

        mockMvc.perform(post("/api/spectacole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllSpectacole() throws Exception {
        when(spectacolService.getAllSpectacole()).thenReturn(List.of(spectacol));

        mockMvc.perform(get("/api/spectacole"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Hamlet"));
    }

    @Test
    void shouldGetSpectacolById() throws Exception {
        when(spectacolService.getSpectacolById(1L)).thenReturn(spectacol);

        mockMvc.perform(get("/api/spectacole/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Hamlet"));
    }

    @Test
    void shouldReturn400WhenSpectacolNotFound() throws Exception {
        when(spectacolService.getSpectacolById(99L))
                .thenThrow(new RuntimeException("Spectacol not found"));

        mockMvc.perform(get("/api/spectacole/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Spectacol not found"));
    }

    @Test
    void shouldUpdateSpectacol() throws Exception {
        UpdateSpectacolRequest req = new UpdateSpectacolRequest();
        req.setTitle("Macbeth");
        req.setDescription("Alta descriere");
        req.setDateTime(LocalDateTime.now().plusDays(5));
        req.setPrice(80.0);

        Spectacol updated = new Spectacol("Macbeth", "Alta descriere",
                LocalDateTime.now().plusDays(5), 80.0, sala);
        updated.setId(1L);

        when(spectacolService.updateSpectacol(eq(1L), any(UpdateSpectacolRequest.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/spectacole/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Macbeth"))
                .andExpect(jsonPath("$.price").value(80.0));
    }

    @Test
    void shouldDeleteSpectacol() throws Exception {
        doNothing().when(spectacolService).deleteSpectacol(1L);

        mockMvc.perform(delete("/api/spectacole/1"))
                .andExpect(status().isNoContent());

        verify(spectacolService, times(1)).deleteSpectacol(1L);
    }
}
