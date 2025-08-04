package ifs.climatech.service.impl;

import ifs.climatech.dto.LeituraSensorCreateDTO;
import ifs.climatech.model.Equipamento;
import ifs.climatech.model.LeituraSensor;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.repository.LeituraSensorRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LeituraSensorServiceTest {

    @Mock
    private LeituraSensorRepository leituraSensorRepository;

    @Mock
    private EquipamentoRepository equipamentoRepository;

    @InjectMocks
    private LeituraSensorServiceImpl leituraSensorService;

    private LeituraSensorCreateDTO dto;
    private Equipamento equipamento;

    @BeforeEach
    void setUp() {
        dto = new LeituraSensorCreateDTO();
        dto.setTagEquipamento("AC-LAB-01");
        dto.setTemperatura(22.5);
        dto.setAmperagem(1.5);
        dto.setVoltagem(219.8);

        equipamento = new Equipamento();
        equipamento.setId(1L);
        equipamento.setTag("AC-LAB-01");
    }

    @Test
    public void deveSalvarLeituraComSucesso() {
        // Dado (Given)
        // Simulamos que o equipamento com a tag foi encontrado
        when(equipamentoRepository.findByTag("AC-LAB-01")).thenReturn(Optional.of(equipamento));

        // Quando (When)
        leituraSensorService.salvar(dto);

        // Então (Then)
        // Verificamos se o método save do repositório de leituras foi chamado exatamente uma vez
        verify(leituraSensorRepository, times(1)).save(any(LeituraSensor.class));

        // Teste avançado: Capturamos o objeto que foi passado para o método save
        ArgumentCaptor<LeituraSensor> leituraCaptor = ArgumentCaptor.forClass(LeituraSensor.class);
        verify(leituraSensorRepository).save(leituraCaptor.capture());

        LeituraSensor leituraSalva = leituraCaptor.getValue();
        assertThat(leituraSalva.getEquipamento().getTag()).isEqualTo("AC-LAB-01");
        assertThat(leituraSalva.getTemperatura()).isEqualTo(22.5);
        assertThat(leituraSalva.getTimestamp()).isNotNull(); // Garante que o timestamp foi setado
    }

    @Test
    public void deveLancarExcecaoSeEquipamentoNaoForEncontrado() {
        // Dado (Given)
        // Simulamos que o equipamento com a tag NÃO foi encontrado
        when(equipamentoRepository.findByTag("TAG-INEXISTENTE")).thenReturn(Optional.empty());
        dto.setTagEquipamento("TAG-INEXISTENTE");

        // Quando & Então (When & Then)
        // Verificamos se a exceção correta é lançada com a mensagem esperada
        assertThatThrownBy(() -> leituraSensorService.salvar(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Nenhum equipamento encontrado com a tag: TAG-INEXISTENTE");

        // Garante que o método save NUNCA foi chamado se o equipamento não foi encontrado
        verify(leituraSensorRepository, never()).save(any(LeituraSensor.class));
    }
}