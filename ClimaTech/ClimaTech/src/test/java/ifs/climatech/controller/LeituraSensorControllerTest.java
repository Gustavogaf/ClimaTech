package ifs.climatech.controller;

import ifs.climatech.dto.LeituraSensorCreateDTO;
import ifs.climatech.service.LeituraSensorService;
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

@WebMvcTest(LeituraSensorController.class)
public class LeituraSensorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeituraSensorService leituraSensorService;

    @Autowired
    private ObjectMapper objectMapper;

    private LeituraSensorCreateDTO dto;

    @BeforeEach
    void setUp() {
        dto = new LeituraSensorCreateDTO();
        dto.setTagEquipamento("TAG-VALIDA");
        dto.setTemperatura(25.0);
    }

    @Test
    public void deveReceberLeituraERetornarStatusAccepted() throws Exception {
        // Dado (Given)
        // Simulamos que o serviço processa a requisição sem erros
        doNothing().when(leituraSensorService).salvar(any(LeituraSensorCreateDTO.class));

        // Quando & Então (When & Then)
        mockMvc.perform(post("/api/leituras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isAccepted()); // Verifica se o status é 202
    }

    @Test
    public void deveRetornarNotFoundQuandoTagDoEquipamentoNaoExiste() throws Exception {
        // Dado (Given)
        String mensagemErro = "Equipamento com a tag TAG-INVALIDA não encontrado.";
        // Simulamos que o serviço lança a exceção quando a tag é inválida
        doThrow(new EntityNotFoundException(mensagemErro))
            .when(leituraSensorService).salvar(any(LeituraSensorCreateDTO.class));

        // Quando & Então (When & Then)
        mockMvc.perform(post("/api/leituras")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound()) // Verifica se o status é 404
                .andExpect(jsonPath("$.erro").value(mensagemErro)); // Verifica a mensagem de erro no JSON
    }
}