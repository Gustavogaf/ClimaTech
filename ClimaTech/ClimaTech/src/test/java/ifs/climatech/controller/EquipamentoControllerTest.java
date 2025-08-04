package ifs.climatech.controller;

import ifs.climatech.dto.EquipamentoDTO;
import ifs.climatech.service.EquipamentoService;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EquipamentoController.class)
public class EquipamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipamentoService equipamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    private EquipamentoDTO equipamentoDTO;

    @BeforeEach
    void setUp() {
        equipamentoDTO = new EquipamentoDTO();
        equipamentoDTO.setId(1L);
        equipamentoDTO.setTag("AC-LABINF-01");
        equipamentoDTO.setMacAddress("AA:BB:CC:DD:EE:FF");
        equipamentoDTO.setIpAddress("10.10.10.10");
        equipamentoDTO.setSalaId(1L);
    }

    // --- TESTES PARA O ENDPOINT POST ---
    @Test
    public void deveCriarEquipamentoERetornarStatusCreated() throws Exception {
        when(equipamentoService.salvar(any(EquipamentoDTO.class))).thenReturn(equipamentoDTO);

        mockMvc.perform(post("/api/equipamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipamentoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tag").value("AC-LABINF-01"));
    }

    @Test
    public void deveRetornarBadRequestAoSalvarComErroDeNegocio() throws Exception {
        when(equipamentoService.salvar(any(EquipamentoDTO.class)))
            .thenThrow(new RegraDeNegocioException("Tag já existe."));

        mockMvc.perform(post("/api/equipamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipamentoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Tag já existe."));
    }

    // --- TESTES PARA O ENDPOINT GET (TODOS) ---
    @Test
    public void deveListarTodosOsEquipamentos() throws Exception {
        when(equipamentoService.listarTodos()).thenReturn(List.of(equipamentoDTO));

        mockMvc.perform(get("/api/equipamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tag").value("AC-LABINF-01"));
    }

    // --- TESTES PARA O ENDPOINT GET (POR ID) ---
    @Test
    public void deveBuscarEquipamentoPorIdComSucesso() throws Exception {
        when(equipamentoService.buscarPorId(1L)).thenReturn(Optional.of(equipamentoDTO));

        mockMvc.perform(get("/api/equipamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarEquipamentoPorIdInexistente() throws Exception {
        when(equipamentoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/equipamentos/99"))
                .andExpect(status().isNotFound());
    }

    // --- TESTES PARA O ENDPOINT PUT ---
    @Test
    public void deveAtualizarEquipamentoComSucesso() throws Exception {
        EquipamentoDTO dtoAtualizado = new EquipamentoDTO();
        dtoAtualizado.setTag("NOVA-TAG");
        when(equipamentoService.atualizar(eq(1L), any(EquipamentoDTO.class))).thenReturn(dtoAtualizado);

        mockMvc.perform(put("/api/equipamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tag").value("NOVA-TAG"));
    }

    @Test
    public void deveRetornarNotFoundAoAtualizarEquipamentoInexistente() throws Exception {
        when(equipamentoService.atualizar(eq(99L), any(EquipamentoDTO.class)))
            .thenThrow(new EntityNotFoundException("Equipamento não encontrado."));

        mockMvc.perform(put("/api/equipamentos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(equipamentoDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Equipamento não encontrado."));
    }

    // --- TESTES PARA O ENDPOINT DELETE ---
    @Test
    public void deveDeletarEquipamentoComSucesso() throws Exception {
        // Para métodos void que não devem fazer nada, usamos doNothing()
        doNothing().when(equipamentoService).deletar(1L);

        mockMvc.perform(delete("/api/equipamentos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deveRetornarNotFoundAoDeletarEquipamentoInexistente() throws Exception {
        // --- AQUI ESTÁ A CORREÇÃO ---
        // Em vez de when(...).thenThrow(...), usamos doThrow(...).when(...) para métodos void
        doThrow(new EntityNotFoundException("Equipamento não encontrado."))
            .when(equipamentoService).deletar(99L);
        // --- FIM DA CORREÇÃO ---
        
        mockMvc.perform(delete("/api/equipamentos/99"))
            .andExpect(status().isNotFound());
    }
}