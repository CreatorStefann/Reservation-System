package com.example.spectacol.controller;

import com.example.spectacol.dto.CreateSalaRequest;
import com.example.spectacol.dto.UpdateSalaRequest;
import com.example.spectacol.exception.GlobalExceptionHandler;
import com.example.spectacol.model.Sala;
import com.example.spectacol.service.SalaService;
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

@WebMvcTest(controllers = SalaController.class)
@Import(GlobalExceptionHandler.class)
class SalaControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @MockitoBean
    private SalaService salaService;

    private Sala sala;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        sala = new Sala("Sala Mare", 6);
        sala.setId(1L);
    }

    @Test
    void shouldCreateSala() throws Exception {
        CreateSalaRequest req = new CreateSalaRequest();
        req.setName("Sala Mare");
        req.setRows(2);
        req.setSeatsPerRow(3);

        when(salaService.createSala(any(CreateSalaRequest.class))).thenReturn(sala);

        mockMvc.perform(post("/api/sali")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sala Mare"))
                .andExpect(jsonPath("$.capacity").value(6));
    }

    @Test
    void shouldReturn400WhenCreateSalaWithBlankName() throws Exception {
        CreateSalaRequest req = new CreateSalaRequest();
        req.setName("");
        req.setRows(2);
        req.setSeatsPerRow(3);

        mockMvc.perform(post("/api/sali")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetAllSali() throws Exception {
        when(salaService.getAllSali()).thenReturn(List.of(sala));

        mockMvc.perform(get("/api/sali"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Sala Mare"));
    }

    @Test
    void shouldGetSalaById() throws Exception {
        when(salaService.getSalaById(1L)).thenReturn(sala);

        mockMvc.perform(get("/api/sali/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sala Mare"));
    }

    @Test
    void shouldReturn400WhenSalaNotFound() throws Exception {
        when(salaService.getSalaById(99L))
                .thenThrow(new RuntimeException("Sala not found"));

        mockMvc.perform(get("/api/sali/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Sala not found"));
    }

    @Test
    void shouldUpdateSala() throws Exception {
        UpdateSalaRequest req = new UpdateSalaRequest();
        req.setName("Sala Mica");
        req.setCapacity(50);

        Sala updated = new Sala("Sala Mica", 50);
        updated.setId(1L);

        when(salaService.updateSala(eq(1L), any(UpdateSalaRequest.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/sali/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sala Mica"))
                .andExpect(jsonPath("$.capacity").value(50));
    }

    @Test
    void shouldDeleteSala() throws Exception {
        doNothing().when(salaService).deleteSala(1L);

        mockMvc.perform(delete("/api/sali/1"))
                .andExpect(status().isNoContent());

        verify(salaService, times(1)).deleteSala(1L);
    }
}
