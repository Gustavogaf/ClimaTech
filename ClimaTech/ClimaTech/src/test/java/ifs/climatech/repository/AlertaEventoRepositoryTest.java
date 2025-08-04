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
public class AlertaEventoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlertaEventoRepository alertaEventoRepository;

    private Equipamento equipamento;
    private Alerta tipoAlerta;

    @BeforeEach
    void setUp() {
        // Prepara o ambiente completo, pois um AlertaEvento depende de um Equipamento e de um Alerta
        Pavilhao p = new Pavilhao();
        p.setNome("Pavilhão Central");
        entityManager.persist(p);

        Sala s = new Sala();
        s.setNome("Sala 203");
        s.setPavilhao(p);
        entityManager.persist(s);

        equipamento = new Equipamento();
        equipamento.setTag("AC-C-203-01");
        equipamento.setMacAddress("DE:AD:BE:EF:00:01");
        equipamento.setIpAddress("192.168.3.15");
        equipamento.setSala(s);
        entityManager.persist(equipamento);

        tipoAlerta = new Alerta();
        tipoAlerta.setCodigoAlerta("TESTE_FALHA");
        tipoAlerta.setDescricao("Um teste de falha.");
        tipoAlerta.setTipo(Alerta.TipoAlerta.FALHA);
        entityManager.persist(tipoAlerta);
    }

    @Test
    public void deveSalvarEEncontrarAlertaEventoComSucesso() {
        // Dado (Given)
        AlertaEvento evento = new AlertaEvento();
        evento.setEquipamento(equipamento);
        evento.setAlerta(tipoAlerta);
        evento.setTimestamp(LocalDateTime.now());
        evento.setMensagem("Equipamento apresentou falha durante o teste.");
        
        AlertaEvento eventoSalvo = entityManager.persistAndFlush(evento);

        // Quando (When)
        Optional<AlertaEvento> resultado = alertaEventoRepository.findById(eventoSalvo.getId());

        // Então (Then)
        assertThat(resultado).isPresent();
        AlertaEvento eventoEncontrado = resultado.get();
        assertThat(eventoEncontrado.getMensagem()).isEqualTo("Equipamento apresentou falha durante o teste.");
        // Valida as associações
        assertThat(eventoEncontrado.getEquipamento().getTag()).isEqualTo("AC-C-203-01");
        assertThat(eventoEncontrado.getAlerta().getCodigoAlerta()).isEqualTo("TESTE_FALHA");
    }
}