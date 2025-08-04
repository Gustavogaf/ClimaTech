package ifs.climatech.repository;

import ifs.climatech.model.Alerta;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class AlertaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlertaRepository alertaRepository;

    @Test
    public void deveEncontrarAlertaPorCodigo() {
        // Dado
        Alerta alerta = new Alerta();
        alerta.setCodigoAlerta("ALTA_TENSAO");
        alerta.setDescricao("Tensão elétrica acima do normal.");
        alerta.setTipo(Alerta.TipoAlerta.ALERTA);
        entityManager.persistAndFlush(alerta);

        // Quando
        Optional<Alerta> resultado = alertaRepository.findByCodigoAlerta("ALTA_TENSAO");

        // Então
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getDescricao()).isEqualTo("Tensão elétrica acima do normal.");
    }

    @Test
    public void deveRetornarVazioSeCodigoDoAlertaNaoExiste() {
        // Quando
        Optional<Alerta> resultado = alertaRepository.findByCodigoAlerta("CODIGO_INEXISTENTE");

        // Então
        assertThat(resultado).isNotPresent();
    }
}