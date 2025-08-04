package ifs.climatech.service.impl;

import ifs.climatech.dto.UsuarioCreateDTO;
import ifs.climatech.dto.UsuarioDTO;
import ifs.climatech.model.Usuario;
import ifs.climatech.repository.UsuarioRepository;
import ifs.climatech.service.exception.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioCreateDTO createDTO;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        createDTO = new UsuarioCreateDTO();
        createDTO.setNome("Ana");
        createDTO.setEmail("ana@email.com");
        createDTO.setSenha("senha123");
        createDTO.setTipo(Usuario.TipoUsuario.ADM);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(createDTO.getNome());
        usuario.setEmail(createDTO.getEmail());
        usuario.setTipo(createDTO.getTipo());
    }

    @Test
    public void deveSalvarUsuarioComSucesso() {
        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.salvar(createDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEqualTo("ana@email.com");
        // Garante que o DTO de resposta não contém a senha
        assertThat(resultado.getClass().getDeclaredFields()).noneMatch(f -> f.getName().equals("senha"));
    }

    @Test
    public void naoDeveSalvarUsuarioComEmailDuplicado() {
        when(usuarioRepository.findByEmail("ana@email.com")).thenReturn(Optional.of(new Usuario()));

        assertThatThrownBy(() -> usuarioService.salvar(createDTO))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessage("O e-mail 'ana@email.com' já está em uso.");
    }

    @Test
    public void deveAtualizarUsuarioComSucesso() {
        UsuarioDTO updateDTO = new UsuarioDTO(usuario);
        updateDTO.setNome("Ana Silva");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.atualizar(1L, updateDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Ana Silva");
    }
}