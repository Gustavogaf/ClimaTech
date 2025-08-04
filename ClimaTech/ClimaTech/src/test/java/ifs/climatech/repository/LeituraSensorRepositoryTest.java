package ifs.climatech.repository;

import ifs.climatech.model.Equipamento;
import ifs.climatech.model.LeituraSensor;
import ifs.climatech.model.Pavilhao;
import ifs.climatech.model.Sala;
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
public class LeituraSensorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LeituraSensorRepository leituraSensorRepository;

    @Test
    public void deveSalvarEEncontrarLeituraComSucesso() {
        // Dado (Given)
        // Preparamos o ambiente completo: Pavilhão -> Sala -> Equipamento
        Pavilhao p = new Pavilhao();
        p.setNome("Pavilhão Teste");
        entityManager.persist(p);

        Sala s = new Sala();
        s.setNome("Sala Teste");
        s.setPavilhao(p);
        entityManager.persist(s);

        Equipamento eq = new Equipamento();
        eq.setTag("TESTE-TAG");
        eq.setMacAddress("AA:AA:AA:AA:AA:AA");
        eq.setIpAddress("1.1.1.1");
        eq.setSala(s);
        entityManager.persist(eq);

        LeituraSensor leitura = new LeituraSensor();
        leitura.setEquipamento(eq);
        leitura.setTimestamp(LocalDateTime.now());
        leitura.setTemperatura(23.5);
        leitura.setAmperagem(1.2);
        leitura.setVoltagem(220.1);
        
        // Salvamos a leitura usando o entityManager para ter um ID para buscar
        LeituraSensor leituraSalva = entityManager.persistAndFlush(leitura);

        // Quando (When)
        Optional<LeituraSensor> resultado = leituraSensorRepository.findById(leituraSalva.getId());

        // Então (Then)
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTemperatura()).isEqualTo(23.5);
        assertThat(resultado.get().getEquipamento().getTag()).isEqualTo("TESTE-TAG");
    }
}