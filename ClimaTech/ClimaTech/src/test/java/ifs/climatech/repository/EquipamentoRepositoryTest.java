package ifs.climatech.repository;

import ifs.climatech.model.Equipamento;
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
public class EquipamentoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @BeforeEach
    void setUp() {
        Pavilhao p = new Pavilhao();
        p.setNome("Pavilhão A");
        entityManager.persist(p);

        Sala s = new Sala();
        s.setNome("Sala 101");
        s.setPavilhao(p);
        entityManager.persist(s);

        Equipamento eq = new Equipamento();
        eq.setTag("AC-A-101-01");
        eq.setMacAddress("00:1B:44:11:3A:B7");
        eq.setIpAddress("192.168.1.10");
        eq.setSala(s);
        entityManager.persistAndFlush(eq);
    }

    @Test
    public void deveRetornarTrueSeMacAddressExiste() {
        boolean existe = equipamentoRepository.existsByMacAddress("00:1B:44:11:3A:B7");
        assertThat(existe).isTrue();
    }

    @Test
    public void deveRetornarFalseSeMacAddressNaoExiste() {
        boolean existe = equipamentoRepository.existsByMacAddress("FF:FF:FF:FF:FF:FF");
        assertThat(existe).isFalse();
    }

    @Test
    public void deveRetornarTrueSeIpAddressExiste() {
        boolean existe = equipamentoRepository.existsByIpAddress("192.168.1.10");
        assertThat(existe).isTrue();
    }

    @Test
    public void deveRetornarFalseSeIpAddressNaoExiste() {
        boolean existe = equipamentoRepository.existsByIpAddress("10.0.0.1");
        assertThat(existe).isFalse();
    }
}