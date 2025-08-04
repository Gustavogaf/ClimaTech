package ifs.climatech.repository;

import ifs.climatech.model.Pavilhao;
import ifs.climatech.model.Sala;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class SalaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SalaRepository salaRepository;

    private Pavilhao pavilhao;

    @BeforeEach
    void setUp() {
        // Cria e persiste um pavilhão base para os testes
        pavilhao = new Pavilhao();
        pavilhao.setNome("Pavilhão de Testes");
        entityManager.persist(pavilhao);
    }

    @Test
    public void deveRetornarTrueSeSalaExisteNoPavilhao() {
        // Dado (Given)
        Sala sala = new Sala();
        sala.setNome("Sala A1");
        sala.setPavilhao(pavilhao);
        entityManager.persistAndFlush(sala);

        // Quando (When)
        boolean existe = salaRepository.existsByNomeAndPavilhao("Sala A1", pavilhao);

        // Então (Then)
        assertThat(existe).isTrue();
    }

    @Test
    public void deveRetornarFalseSeSalaNaoExisteNoPavilhao() {
        // Dado (Given) - Nenhuma sala é criada

        // Quando (When)
        boolean existe = salaRepository.existsByNomeAndPavilhao("Sala A1", pavilhao);

        // Então (Then)
        assertThat(existe).isFalse();
    }
}