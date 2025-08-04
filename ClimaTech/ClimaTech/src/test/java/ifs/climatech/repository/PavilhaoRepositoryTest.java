package ifs.climatech.repository;

import ifs.climatech.model.Pavilhao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PavilhaoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PavilhaoRepository pavilhaoRepository;

    @Test
    public void quandoBuscarPorId_entaoRetornarPavilhao() {
        // Dado (Given)
        Pavilhao pavilhao = new Pavilhao();
        pavilhao.setNome("Pavilhão de Teste");
        entityManager.persist(pavilhao);
        entityManager.flush();

        // Quando (When)
        Pavilhao encontrado = pavilhaoRepository.findById(pavilhao.getId()).orElse(null);

        // Então (Then)
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNome()).isEqualTo(pavilhao.getNome());
    }
}
