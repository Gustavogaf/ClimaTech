package ifs.climatech.service.impl;

import ifs.climatech.dto.EquipamentoStatusDTO;
import ifs.climatech.model.Equipamento;
import ifs.climatech.model.LeituraSensor;
import ifs.climatech.model.RegistroUso;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.repository.LeituraSensorRepository;
import ifs.climatech.repository.RegistroUsoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MetricasServiceTest {

    @Mock
    private EquipamentoRepository equipamentoRepository;
    @Mock
    private RegistroUsoRepository registroUsoRepository;
    @Mock
    private LeituraSensorRepository leituraSensorRepository;

    @InjectMocks
    private MetricasServiceImpl metricasService;

    private Equipamento equipamento;

    @BeforeEach
    void setUp() {
        equipamento = new Equipamento();
        equipamento.setId(1L);
        equipamento.setTag("AC-AUDITORIO");
    }

    @Test
    public void deveCalcularMetricasCorretamenteComDadosSimulados() {
        // --- PREPARAÇÃO DA MASSA DE DADOS (15 minutos) ---
        LocalDateTime agora = LocalDateTime.now();
        List<LeituraSensor> leiturasSimuladas = new ArrayList<>();
        // Geramos 30 leituras, uma a cada 30 segundos, de trás para frente no tempo
        for (int i = 0; i < 30; i++) {
            LeituraSensor leitura = new LeituraSensor();
            leitura.setTimestamp(agora.minusSeconds(i * 30));
            // A última leitura (i=0) terá a temperatura 25.0, a penúltima 24.9, etc.
            leitura.setTemperatura(25.0 - (i * 0.1));
            leitura.setVoltagem(220.0);
            leitura.setAmperagem(5.0);
            leiturasSimuladas.add(leitura);
        }
        
        // Simulamos um registro de uso que começou há 15 minutos
        RegistroUso registroAberto = new RegistroUso();
        registroAberto.setDataHoraInicio(agora.minusMinutes(15));
        
        // Dado (Given)
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamento));
        when(registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento)).thenReturn(Optional.of(registroAberto));
        // O repositório retornará nossa lista simulada
        when(leituraSensorRepository.findByEquipamentoOrderByTimestampDesc(any(Equipamento.class), any(Pageable.class)))
            .thenReturn(leiturasSimuladas.stream().limit(10).toList()); // O serviço pede as 10 últimas

        // Quando (When)
        EquipamentoStatusDTO dto = metricasService.getStatusAtual(1L);

        // Então (Then)
        assertThat(dto).isNotNull();
        assertThat(dto.isLigado()).isTrue();
        // A temperatura atual deve ser a da leitura mais recente (i=0)
        assertThat(dto.getTemperaturaAtual()).isEqualTo(25.0);
        // O tempo de uso deve ser aproximadamente 15 minutos
        assertThat(dto.getTempoDeUsoAtual()).matches("\\d+h 15m");
        
        // Valida o cálculo de consumo (Potência = 220V * 5A = 1100W = 1.1kW)
        // Consumo em 1h = 1.1 kWh
        assertThat(dto.getConsumoEnergiaUltimas24h()).isEqualTo(1.1);
    }
}