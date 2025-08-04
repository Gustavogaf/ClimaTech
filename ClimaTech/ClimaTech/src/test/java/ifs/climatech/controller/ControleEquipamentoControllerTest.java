package ifs.climatech.controller;

import ifs.climatech.dto.ControleEquipamentoDTO;
import ifs.climatech.service.RegistroUsoService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControleEquipamentoController.class)
public class ControleEquipamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistroUsoService registroUsoService;

    @Autowired
    private ObjectMapper objectMapper;

    private ControleEquipamentoDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ControleEquipamentoDTO();
        dto.setEquipamentoId(1L);
        dto.setUsuarioId(1L);
    }

    // --- TESTES PARA O ENDPOINT /ligar ---

    @Test
    public void deveRetornarOkAoLigarEquipamento() throws Exception {
        doNothing().when(registroUsoService).ligar(any(ControleEquipamentoDTO.class));

        mockMvc.perform(post("/api/controle/ligar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void deveRetornarBadRequestAoTentarLigarEquipamentoJaLigado() throws Exception {
        String msgErro = "Este equipamento já está ligado.";
        doThrow(new RegraDeNegocioException(msgErro))
            .when(registroUsoService).ligar(any(ControleEquipamentoDTO.class));

        mockMvc.perform(post("/api/controle/ligar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value(msgErro));
    }

    // --- TESTES PARA O ENDPOINT /desligar ---

    @Test
    public void deveRetornarOkAoDesligarEquipamento() throws Exception {
        doNothing().when(registroUsoService).desligar(any(ControleEquipamentoDTO.class));

        mockMvc.perform(post("/api/controle/desligar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void deveRetornarBadRequestAoTentarDesligarEquipamentoJaDesligado() throws Exception {
        String msgErro = "Este equipamento já está desligado.";
        doThrow(new RegraDeNegocioException(msgErro))
            .when(registroUsoService).desligar(any(ControleEquipamentoDTO.class));

        mockMvc.perform(post("/api/controle/desligar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value(msgErro));
    }
    
    @Test
    public void deveRetornarNotFoundSeEquipamentoNaoExiste() throws Exception {
        String msgErro = "Equipamento não encontrado.";
        doThrow(new EntityNotFoundException(msgErro))
            .when(registroUsoService).ligar(any(ControleEquipamentoDTO.class));

        mockMvc.perform(post("/api/controle/ligar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value(msgErro));
    }
}