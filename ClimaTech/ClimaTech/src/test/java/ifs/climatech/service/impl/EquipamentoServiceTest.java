package ifs.climatech.service.impl;

import ifs.climatech.dto.EquipamentoDTO;
import ifs.climatech.model.Equipamento;
import ifs.climatech.model.Sala;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.repository.SalaRepository;
import ifs.climatech.service.exception.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EquipamentoServiceTest {

    @Mock
    private EquipamentoRepository equipamentoRepository;
    @Mock
    private SalaRepository salaRepository;
    @InjectMocks
    private EquipamentoServiceImpl equipamentoService;

    private Sala sala;
    private EquipamentoDTO dto;

    @BeforeEach
    void setUp() {
        sala = new Sala();
        sala.setId(1L);

        dto = new EquipamentoDTO();
        dto.setTag("TAG-01");
        dto.setMacAddress("00:00:00:00:00:01");
        dto.setIpAddress("192.168.0.1");
        dto.setSalaId(1L);
    }

    @Test
    public void deveSalvarEquipamentoComSucesso() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(equipamentoRepository.save(any(Equipamento.class))).thenAnswer(i -> i.getArgument(0));

        EquipamentoDTO resultado = equipamentoService.salvar(dto);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getTag()).isEqualTo("TAG-01");
    }

    @Test
    public void naoDeveSalvarComTagDuplicada() {
        when(equipamentoRepository.findByTag("TAG-01")).thenReturn(Optional.of(new Equipamento()));

        assertThatThrownBy(() -> equipamentoService.salvar(dto))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessage("A tag 'TAG-01' já está em uso.");
    }

    @Test
    public void naoDeveSalvarComMacAddressDuplicado() {
        when(equipamentoRepository.existsByMacAddress("00:00:00:00:00:01")).thenReturn(true);

        assertThatThrownBy(() -> equipamentoService.salvar(dto))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessage("O Endereço MAC '00:00:00:00:00:01' já está cadastrado.");
    }

    @Test
    public void naoDeveSalvarComIpAddressDuplicado() {
        when(equipamentoRepository.existsByIpAddress("192.168.0.1")).thenReturn(true);

        assertThatThrownBy(() -> equipamentoService.salvar(dto))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessage("O Endereço IP '192.168.0.1' já está em uso.");
    }
}