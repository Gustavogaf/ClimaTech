package ifs.climatech.service.impl;

import ifs.climatech.dto.AlertaDTO;
import ifs.climatech.model.Alerta;
import ifs.climatech.repository.AlertaRepository;
import ifs.climatech.service.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlertaServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    @InjectMocks
    private AlertaServiceImpl alertaService;

    private AlertaDTO alertaDTO;
    private Alerta alerta;

    @BeforeEach
    void setUp() {
        alerta = new Alerta();
        alerta.setId(1L);
        alerta.setCodigoAlerta("FALHA_COMPRESSOR");
        alerta.setDescricao("Compressor do equipamento parou de funcionar.");
        alerta.setTipo(Alerta.TipoAlerta.FALHA);
        
        alertaDTO = new AlertaDTO(alerta);
    }

    @Test
    public void deveSalvarAlertaComSucesso() {
        when(alertaRepository.findByCodigoAlerta("FALHA_COMPRESSOR")).thenReturn(Optional.empty());
        when(alertaRepository.save(any(Alerta.class))).thenReturn(alerta);

        AlertaDTO resultado = alertaService.salvar(alertaDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCodigoAlerta()).isEqualTo("FALHA_COMPRESSOR");
    }

    @Test
    public void naoDeveSalvarAlertaComCodigoDuplicado() {
        when(alertaRepository.findByCodigoAlerta("FALHA_COMPRESSOR")).thenReturn(Optional.of(new Alerta()));

        assertThatThrownBy(() -> alertaService.salvar(alertaDTO))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessage("O código de alerta 'FALHA_COMPRESSOR' já existe.");
    }
    
    @Test
    public void deveDeletarAlertaComSucesso() {
        when(alertaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(alertaRepository).deleteById(1L);

        alertaService.deletar(1L);

        verify(alertaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deveLancarExcecaoAoDeletarAlertaInexistente() {
        when(alertaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> alertaService.deletar(99L))
            .isInstanceOf(EntityNotFoundException.class);
    }
}
