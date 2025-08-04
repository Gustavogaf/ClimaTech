package ifs.climatech.controller;

import ifs.climatech.dto.PavilhaoDTO;
import ifs.climatech.model.Pavilhao;
import ifs.climatech.service.PavilhaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PavilhaoController.class)
public class PavilhaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PavilhaoService pavilhaoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pavilhao pavilhao;
    private PavilhaoDTO pavilhaoDTO;

    @BeforeEach
    void setUp() {
        pavilhao = new Pavilhao();
        pavilhao.setId(1L);
        pavilhao.setNome("Bloco A");
        pavilhaoDTO = new PavilhaoDTO(pavilhao);
    }

    @Test
    public void testListarTodos() throws Exception {
        // Dado
        when(pavilhaoService.listarTodos()).thenReturn(Collections.singletonList(pavilhao));

        // Quando & Então
        mockMvc.perform(get("/api/pavilhoes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nome").value("Bloco A"));
    }

    @Test
    public void testSalvar() throws Exception {
        // Dado
        when(pavilhaoService.salvar(any(Pavilhao.class))).thenReturn(pavilhao);

        // Quando & Então
        mockMvc.perform(post("/api/pavilhoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pavilhaoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Bloco A"));
    }
}

