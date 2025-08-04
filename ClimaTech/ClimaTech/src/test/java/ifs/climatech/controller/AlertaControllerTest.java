package ifs.climatech.controller;

import ifs.climatech.dto.AlertaDTO;
import ifs.climatech.model.Alerta;
import ifs.climatech.service.AlertaService;
import ifs.climatech.service.exception.RegraDeNegocioException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlertaController.class)
public class AlertaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlertaService alertaService;

    @Autowired
    private ObjectMapper objectMapper;

    private AlertaDTO alertaDTO;

    @BeforeEach
    void setUp() {
        alertaDTO = new AlertaDTO();
        alertaDTO.setId(1L);
        alertaDTO.setCodigoAlerta("CONSUMO_ALTO");
        alertaDTO.setDescricao("Consumo de energia acima da média.");
        alertaDTO.setTipo(Alerta.TipoAlerta.ALERTA);
        alertaDTO.setAtivo(true);
    }

    @Test
    public void deveCriarAlertaERetornarStatusCreated() throws Exception {
        when(alertaService.salvar(any(AlertaDTO.class))).thenReturn(alertaDTO);

        mockMvc.perform(post("/api/alertas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alertaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigoAlerta").value("CONSUMO_ALTO"));
    }

    @Test
    public void deveRetornarBadRequestAoSalvarComCodigoExistente() throws Exception {
        when(alertaService.salvar(any(AlertaDTO.class)))
            .thenThrow(new RegraDeNegocioException("Código já existe."));

        mockMvc.perform(post("/api/alertas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alertaDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Código já existe."));
    }
    
    @Test
    public void deveListarTodosOsAlertas() throws Exception {
        when(alertaService.listarTodos()).thenReturn(List.of(alertaDTO));

        mockMvc.perform(get("/api/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoAlerta").value("CONSUMO_ALTO"));
    }

    @Test
    public void deveBuscarAlertaPorId() throws Exception {
        when(alertaService.buscarPorId(1L)).thenReturn(Optional.of(alertaDTO));

        mockMvc.perform(get("/api/alertas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void deveAtualizarAlerta() throws Exception {
        when(alertaService.atualizar(eq(1L), any(AlertaDTO.class))).thenReturn(alertaDTO);

        mockMvc.perform(put("/api/alertas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alertaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoAlerta").value("CONSUMO_ALTO"));
    }

    @Test
    public void deveDeletarAlerta() throws Exception {
        mockMvc.perform(delete("/api/alertas/1"))
                .andExpect(status().isNoContent());
    }
}