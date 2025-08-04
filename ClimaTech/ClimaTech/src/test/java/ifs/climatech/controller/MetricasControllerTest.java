package ifs.climatech.controller;

import ifs.climatech.dto.EquipamentoStatusDTO;
import ifs.climatech.service.MetricasService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MetricasController.class)
public class MetricasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricasService metricasService;

    @Autowired
    private ObjectMapper objectMapper;

    private EquipamentoStatusDTO statusDTO;

    @BeforeEach
    void setUp() {
        statusDTO = new EquipamentoStatusDTO();
        statusDTO.setEquipamentoId(1L);
        statusDTO.setTag("AC-SALA-REUNIOES");
        statusDTO.setLigado(true);
        statusDTO.setTemperaturaAtual(22.0);
        statusDTO.setConsumoEnergiaUltimas24h(3.4);
    }

    @Test
    public void deveRetornarMetricasDoEquipamentoComSucesso() throws Exception {
        // Dado (Given)
        // Simulamos que o serviço retorna nosso DTO de teste quando chamado com o ID 1
        when(metricasService.getStatusAtual(1L)).thenReturn(statusDTO);

        // Quando & Então (When & Then)
        mockMvc.perform(get("/api/metricas/equipamento/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica o status 200
                .andExpect(jsonPath("$.tag").value("AC-SALA-REUNIOES"))
                .andExpect(jsonPath("$.ligado").value(true))
                .andExpect(jsonPath("$.temperaturaAtual").value(22.0));
    }

    @Test
    public void deveRetornarNotFoundQuandoEquipamentoNaoExiste() throws Exception {
        // Dado (Given)
        String msgErro = "Equipamento com ID 99 não foi encontrado.";
        // Simulamos que o serviço lança a exceção ao ser chamado com um ID inválido
        when(metricasService.getStatusAtual(99L)).thenThrow(new EntityNotFoundException(msgErro));

        // Quando & Então (When & Then)
        mockMvc.perform(get("/api/metricas/equipamento/99")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()) // Verifica o status 404
                .andExpect(jsonPath("$.erro").value(msgErro)); // Verifica a mensagem de erro
    }
}