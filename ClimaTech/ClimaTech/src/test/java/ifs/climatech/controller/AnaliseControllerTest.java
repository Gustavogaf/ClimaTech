package ifs.climatech.controller;

import ifs.climatech.dto.AlertaEventoDTO;
import ifs.climatech.service.AnaliseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnaliseController.class)
public class AnaliseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnaliseService analiseService;

    @Autowired
    private ObjectMapper objectMapper;

    private AlertaEventoDTO alertaEventoDTO;

    @BeforeEach
    void setUp() {
        alertaEventoDTO = new AlertaEventoDTO();
        alertaEventoDTO.setId(1L);
        alertaEventoDTO.setCodigoAlerta("CONSUMO_ALTO");
        alertaEventoDTO.setTagEquipamento("AC-01");
        alertaEventoDTO.setTimestamp(LocalDateTime.now());
    }

    @Test
    public void deveRetornarOkComEventoQuandoAlertaEhGerado() throws Exception {
        // Dado
        when(analiseService.analisarEquipamento(1L)).thenReturn(Optional.of(alertaEventoDTO));

        // Quando & Então
        mockMvc.perform(post("/api/analise/verificar")
                .param("equipamentoId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoAlerta").value("CONSUMO_ALTO"));
    }

    @Test
    public void deveRetornarNoContentQuandoNenhumAlertaEhGerado() throws Exception {
        // Dado
        when(analiseService.analisarEquipamento(1L)).thenReturn(Optional.empty());

        // Quando & Então
        mockMvc.perform(post("/api/analise/verificar")
                .param("equipamentoId", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deveRetornarNotFoundQuandoEquipamentoNaoExiste() throws Exception {
        // Dado
        String msgErro = "Equipamento não encontrado.";
        when(analiseService.analisarEquipamento(99L)).thenThrow(new EntityNotFoundException(msgErro));

        // Quando & Então
        mockMvc.perform(post("/api/analise/verificar")
                .param("equipamentoId", "99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value(msgErro));
    }
}