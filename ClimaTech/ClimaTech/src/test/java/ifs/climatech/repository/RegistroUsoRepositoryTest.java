package ifs.climatech.repository;

import ifs.climatech.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RegistroUsoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RegistroUsoRepository registroUsoRepository;

    private Equipamento equipamento;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // --- INÍCIO DA CORREÇÃO ---
        Pavilhao p = new Pavilhao();
        p.setNome("Pavilhão Teste BDD"); // Adicionado o nome
        entityManager.persist(p);

        Sala s = new Sala();
        s.setNome("Sala Teste BDD"); // Adicionado o nome
        s.setPavilhao(p);
        entityManager.persist(s);
        // --- FIM DA CORREÇÃO ---

        equipamento = new Equipamento();
        equipamento.setTag("EQ-TESTE");
        equipamento.setMacAddress("AA:AA:AA:AA:AA:AA");
        equipamento.setIpAddress("1.1.1.1");
        equipamento.setSala(s);
        entityManager.persist(equipamento);

        usuario = new Usuario();
        usuario.setNome("Usuário de Teste"); // Boa prática adicionar todos os campos não nulos
        usuario.setEmail("teste@email.com");
        usuario.setSenha("123");
        usuario.setTipo(Usuario.TipoUsuario.COMUM);
        entityManager.persist(usuario);
    }

    @Test
    public void deveEncontrarRegistroDeUsoAberto() {
        // Dado
        RegistroUso registroAberto = new RegistroUso();
        registroAberto.setEquipamento(equipamento);
        registroAberto.setUsuario(usuario);
        registroAberto.setDataHoraInicio(LocalDateTime.now());
        registroAberto.setDataHoraFim(null);
        entityManager.persistAndFlush(registroAberto);

        // Quando
        Optional<RegistroUso> resultado = registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento);

        // Então
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(registroAberto.getId());
    }

    @Test
    public void naoDeveEncontrarRegistroDeUsoAbertoSeEquipamentoEstaDesligado() {
        // Dado
        RegistroUso registroFechado = new RegistroUso();
        registroFechado.setEquipamento(equipamento);
        registroFechado.setUsuario(usuario);
        registroFechado.setDataHoraInicio(LocalDateTime.now().minusHours(1));
        registroFechado.setDataHoraFim(LocalDateTime.now());
        entityManager.persistAndFlush(registroFechado);

        // Quando
        Optional<RegistroUso> resultado = registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento);

        // Então
        assertThat(resultado).isNotPresent();
    }
}