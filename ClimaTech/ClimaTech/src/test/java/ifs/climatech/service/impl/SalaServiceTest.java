package ifs.climatech.service.impl;

import ifs.climatech.dto.SalaDTO;
import ifs.climatech.model.Pavilhao;
import ifs.climatech.model.Sala;
import ifs.climatech.repository.PavilhaoRepository;
import ifs.climatech.repository.SalaRepository;
import ifs.climatech.service.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalaServiceTest {

    @Mock
    private SalaRepository salaRepository;
    @Mock
    private PavilhaoRepository pavilhaoRepository;
    @InjectMocks
    private SalaServiceImpl salaService;

    private Pavilhao pavilhao;
    private Sala sala;
    private SalaDTO salaDTO;

    @BeforeEach
    void setUp() {
        pavilhao = new Pavilhao();
        pavilhao.setId(1L);
        pavilhao.setNome("Pavilhão Principal");

        sala = new Sala();
        sala.setId(1L);
        sala.setNome("Sala 101");
        sala.setPavilhao(pavilhao);

        salaDTO = new SalaDTO(sala);
    }

    @Test
    public void deveSalvarSalaComSucesso() {
        when(pavilhaoRepository.findById(1L)).thenReturn(Optional.of(pavilhao));
        when(salaRepository.existsByNomeAndPavilhao("Sala 101", pavilhao)).thenReturn(false);
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        SalaDTO resultado = salaService.salvar(salaDTO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Sala 101");
        verify(salaRepository, times(1)).save(any(Sala.class));
    }

    @Test
    public void naoDeveSalvarSalaComNomeDuplicado() {
        when(pavilhaoRepository.findById(1L)).thenReturn(Optional.of(pavilhao));
        when(salaRepository.existsByNomeAndPavilhao("Sala 101", pavilhao)).thenReturn(true);

        assertThatThrownBy(() -> salaService.salvar(salaDTO))
            .isInstanceOf(RegraDeNegocioException.class);
    }

    @Test
    public void deveListarTodasAsSalas() {
        when(salaRepository.findAll()).thenReturn(List.of(sala));

        List<SalaDTO> resultados = salaService.listarTodas();

        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getNome()).isEqualTo("Sala 101");
    }

    @Test
    public void deveBuscarSalaPorIdComSucesso() {
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));

        Optional<SalaDTO> resultado = salaService.buscarPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }
    
    @Test
    public void deveAtualizarSalaComSucesso() {
        SalaDTO dtoAtualizado = new SalaDTO();
        dtoAtualizado.setNome("Sala 102");
        dtoAtualizado.setPavilhaoId(1L);

        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(pavilhaoRepository.findById(1L)).thenReturn(Optional.of(pavilhao));
        when(salaRepository.existsByNomeAndPavilhao("Sala 102", pavilhao)).thenReturn(false);
        when(salaRepository.save(any(Sala.class))).thenReturn(sala);

        SalaDTO resultado = salaService.atualizar(1L, dtoAtualizado);

        assertThat(resultado).isNotNull();
        verify(salaRepository).save(sala);
    }
    
    @Test
    public void deveLancarExcecaoAoTentarAtualizarSalaInexistente() {
        when(salaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> salaService.atualizar(99L, salaDTO))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void deveDeletarSalaComSucesso() {
        when(salaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(salaRepository).deleteById(1L);

        salaService.deletar(1L);

        verify(salaRepository, times(1)).deleteById(1L);
    }
    
    @Test
    public void deveLancarExcecaoAoTentarDeletarSalaInexistente() {
        when(salaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> salaService.deletar(99L))
            .isInstanceOf(EntityNotFoundException.class);
    }
}