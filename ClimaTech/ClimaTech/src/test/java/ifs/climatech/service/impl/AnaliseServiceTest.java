package ifs.climatech.service.impl;

import ifs.climatech.dto.AlertaEventoDTO;
import ifs.climatech.dto.EquipamentoStatusDTO;
import ifs.climatech.model.Alerta;
import ifs.climatech.model.AlertaEvento;
import ifs.climatech.model.Equipamento;
import ifs.climatech.repository.AlertaEventoRepository;
import ifs.climatech.repository.AlertaRepository;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.service.MetricasService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnaliseServiceTest {

    @Mock
    private MetricasService metricasService;
    @Mock
    private AlertaRepository alertaRepository;
    @Mock
    private EquipamentoRepository equipamentoRepository;
    @Mock
    private AlertaEventoRepository alertaEventoRepository;

    @InjectMocks
    private AnaliseServiceImpl analiseService;

    private EquipamentoStatusDTO statusDTO;
    private Equipamento equipamento;

    @BeforeEach
    void setUp() {
        statusDTO = new EquipamentoStatusDTO();
        equipamento = new Equipamento();
        equipamento.setId(1L);
    }

    @Test
    public void deveDispararAlertaDeConsumoAlto() {
        // Dado
        statusDTO.setConsumoEnergiaUltimas24h(15.5); // Valor acima do limite de 10.0
        Alerta tipoAlerta = new Alerta();
        tipoAlerta.setCodigoAlerta("CONSUMO_ALTO");
        
        when(metricasService.getStatusAtual(1L)).thenReturn(statusDTO);
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamento));
        when(alertaRepository.findByCodigoAlerta("CONSUMO_ALTO")).thenReturn(Optional.of(tipoAlerta));
        when(alertaEventoRepository.save(any(AlertaEvento.class))).thenAnswer(i -> i.getArgument(0));

        // Quando
        Optional<AlertaEventoDTO> resultado = analiseService.analisarEquipamento(1L);

        // Então
        assertThat(resultado).isPresent();
        
        ArgumentCaptor<AlertaEvento> eventoCaptor = ArgumentCaptor.forClass(AlertaEvento.class);
        verify(alertaEventoRepository).save(eventoCaptor.capture());
        
        AlertaEvento eventoSalvo = eventoCaptor.getValue();
        assertThat(eventoSalvo.getAlerta().getCodigoAlerta()).isEqualTo("CONSUMO_ALTO");
        assertThat(eventoSalvo.getMensagem()).contains("15.5 kWh");
    }

    @Test
    public void naoDeveDispararAlertaSeMetricasEstaoNormais() {
        // Dado
        statusDTO.setLigado(true);
        statusDTO.setTemperaturaAtual(23.0); // Temperatura normal
        statusDTO.setConsumoEnergiaUltimas24h(5.0); // Consumo normal
        
        when(metricasService.getStatusAtual(1L)).thenReturn(statusDTO);

        // Quando
        Optional<AlertaEventoDTO> resultado = analiseService.analisarEquipamento(1L);

        // Então
        assertThat(resultado).isNotPresent();
        verify(alertaEventoRepository, never()).save(any(AlertaEvento.class));
    }
    
    @Test
    public void deveLancarExcecaoSeTipoDeAlertaNaoEstaCadastrado() {
        // Dado
        statusDTO.setConsumoEnergiaUltimas24h(12.0); // Viola a regra de consumo
        when(metricasService.getStatusAtual(1L)).thenReturn(statusDTO);
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamento));
        // Simula que o alerta "CONSUMO_ALTO" não foi encontrado no banco de dados
        when(alertaRepository.findByCodigoAlerta("CONSUMO_ALTO")).thenReturn(Optional.empty());

        // Então
        assertThatThrownBy(() -> analiseService.analisarEquipamento(1L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Tipo de alerta com código 'CONSUMO_ALTO' não cadastrado.");
    }
}