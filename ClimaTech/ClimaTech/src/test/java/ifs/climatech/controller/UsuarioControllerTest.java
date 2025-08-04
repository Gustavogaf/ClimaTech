package ifs.climatech.controller;

import ifs.climatech.dto.UsuarioCreateDTO;
import ifs.climatech.dto.UsuarioDTO;
import ifs.climatech.model.Usuario;
import ifs.climatech.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioCreateDTO createDTO;
    private UsuarioDTO responseDTO;

    @BeforeEach
    void setUp() {
        createDTO = new UsuarioCreateDTO();
        createDTO.setNome("Carlos");
        createDTO.setEmail("carlos@email.com");
        createDTO.setSenha("123456");
        createDTO.setTipo(Usuario.TipoUsuario.COMUM);

        responseDTO = new UsuarioDTO();
        responseDTO.setId(1L);
        responseDTO.setNome("Carlos");
        responseDTO.setEmail("carlos@email.com");
        responseDTO.setTipo(Usuario.TipoUsuario.COMUM);
    }

    // --- TESTES PARA O ENDPOINT POST ---
    @Test
    public void deveCriarUsuarioERetornarStatusCreated() throws Exception {
        when(usuarioService.salvar(any(UsuarioCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("carlos@email.com"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    public void deveRetornarBadRequestAoSalvarComEmailExistente() throws Exception {
        when(usuarioService.salvar(any(UsuarioCreateDTO.class)))
            .thenThrow(new RegraDeNegocioException("E-mail já em uso."));

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("E-mail já em uso."));
    }

    // --- TESTES PARA O ENDPOINT GET (TODOS) ---
    @Test
    public void deveListarTodosOsUsuarios() throws Exception {
        when(usuarioService.listarTodos()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("carlos@email.com"));
    }

    // --- TESTES PARA O ENDPOINT GET (POR ID) ---
    @Test
    public void deveBuscarUsuarioPorIdComSucesso() throws Exception {
        when(usuarioService.buscarPorId(1L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void deveRetornarNotFoundAoBuscarUsuarioInexistente() throws Exception {
        when(usuarioService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    // --- TESTES PARA O ENDPOINT PUT ---
    @Test
    public void deveAtualizarUsuarioComSucesso() throws Exception {
        when(usuarioService.atualizar(eq(1L), any(UsuarioDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("carlos@email.com"));
    }

    @Test
    public void deveRetornarNotFoundAoTentarAtualizarUsuarioInexistente() throws Exception {
        when(usuarioService.atualizar(eq(99L), any(UsuarioDTO.class)))
            .thenThrow(new EntityNotFoundException("Usuário não encontrado."));

        mockMvc.perform(put("/api/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responseDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Usuário não encontrado."));
    }

    // --- TESTES PARA O ENDPOINT DELETE ---
    @Test
    public void deveDeletarUsuarioComSucesso() throws Exception {
        doNothing().when(usuarioService).deletar(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deveRetornarNotFoundAoTentarDeletarUsuarioInexistente() throws Exception {
        doThrow(new EntityNotFoundException("Usuário não encontrado."))
            .when(usuarioService).deletar(99L);

        mockMvc.perform(delete("/api/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Usuário não encontrado."));
    }
}