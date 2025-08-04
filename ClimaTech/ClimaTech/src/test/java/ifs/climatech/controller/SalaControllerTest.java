package ifs.climatech.controller;

import ifs.climatech.dto.SalaDTO;
import ifs.climatech.service.SalaService;
import ifs.climatech.service.exception.RegraDeNegocioException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SalaController.class)
public class SalaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SalaService salaService;
    @Autowired
    private ObjectMapper objectMapper;

    private SalaDTO salaDTO;

    @BeforeEach
    void setUp() {
        salaDTO = new SalaDTO();
        salaDTO.setId(1L);
        salaDTO.setPavilhaoId(1L);
        salaDTO.setNome("Sala A-01");
    }

    @Test
    public void deveCriarSalaERetornarStatusCreated() throws Exception {
        when(salaService.salvar(any(SalaDTO.class))).thenReturn(salaDTO);

        mockMvc.perform(post("/api/salas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Sala A-01"));
    }

    @Test
    public void deveRetornarBadRequestAoViolarRegraDeNegocio() throws Exception {
        when(salaService.salvar(any(SalaDTO.class)))
            .thenThrow(new RegraDeNegocioException("Sala já existe."));

        mockMvc.perform(post("/api/salas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salaDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Sala já existe."));
    }

    @Test
    public void deveListarTodasAsSalas() throws Exception {
        when(salaService.listarTodas()).thenReturn(List.of(salaDTO));

        mockMvc.perform(get("/api/salas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    public void deveBuscarSalaPorId() throws Exception {
        when(salaService.buscarPorId(1L)).thenReturn(Optional.of(salaDTO));

        mockMvc.perform(get("/api/salas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Sala A-01"));
    }
    
    @Test
    public void deveRetornarNotFoundAoBuscarSalaInexistente() throws Exception {
        when(salaService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/salas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deveAtualizarSala() throws Exception {
        when(salaService.atualizar(eq(1L), any(SalaDTO.class))).thenReturn(salaDTO);

        mockMvc.perform(put("/api/salas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Sala A-01"));
    }

    @Test
    public void deveDeletarSala() throws Exception {
        doNothing().when(salaService).deletar(1L);

        mockMvc.perform(delete("/api/salas/1"))
                .andExpect(status().isNoContent());
    }
}