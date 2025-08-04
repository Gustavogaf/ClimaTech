package ifs.climatech.repository;

import ifs.climatech.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void deveEncontrarUsuarioPorEmail() {
        // Dado
        Usuario usuario = new Usuario();
        usuario.setNome("Usuario Teste");
        usuario.setEmail("teste@email.com");
        usuario.setSenha("123");
        usuario.setTipo(Usuario.TipoUsuario.COMUM);
        entityManager.persistAndFlush(usuario);

        // Quando
        Optional<Usuario> resultado = usuarioRepository.findByEmail("teste@email.com");

        // Então
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Usuario Teste");
    }

    @Test
    public void deveRetornarVazioSeEmailNaoExiste() {
        // Quando
        Optional<Usuario> resultado = usuarioRepository.findByEmail("naoexiste@email.com");

        // Então
        assertThat(resultado).isNotPresent();
    }
}