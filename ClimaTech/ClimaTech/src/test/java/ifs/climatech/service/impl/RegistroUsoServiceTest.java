package ifs.climatech.service.impl;

import ifs.climatech.dto.ControleEquipamentoDTO;
import ifs.climatech.model.Equipamento;
import ifs.climatech.model.RegistroUso;
import ifs.climatech.model.Usuario;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.repository.RegistroUsoRepository;
import ifs.climatech.repository.UsuarioRepository;
import ifs.climatech.service.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistroUsoServiceTest {

    @Mock
    private RegistroUsoRepository registroUsoRepository;
    @Mock
    private EquipamentoRepository equipamentoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RegistroUsoServiceImpl registroUsoService;

    private Equipamento equipamento;
    private Usuario usuario;
    private ControleEquipamentoDTO dto;

    @BeforeEach
    void setUp() {
        equipamento = new Equipamento();
        equipamento.setId(1L);

        usuario = new Usuario();
        usuario.setId(1L);

        dto = new ControleEquipamentoDTO();
        dto.setEquipamentoId(1L);
        dto.setUsuarioId(1L);
    }

    // --- TESTES PARA O MÉTODO 'LIGAR' ---

    @Test
    public void deveLigarEquipamentoComSucesso() {
        // Dado
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamento));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // Garante que o equipamento está desligado
        when(registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento)).thenReturn(Optional.empty());

        // Quando
        registroUsoService.ligar(dto);

        // Então
        // Verifica que um novo registro foi salvo
        verify(registroUsoRepository, times(1)).save(any(RegistroUso.class));
    }

    @Test
    public void deveLancarExcecaoAoLigarEquipamentoJaLigado() {
        // Dado
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamento));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // Simula que o equipamento já tem um registro de uso aberto
        when(registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento)).thenReturn(Optional.of(new RegistroUso()));

        // Então
        assertThatThrownBy(() -> registroUsoService.ligar(dto))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessage("Este equipamento já está ligado.");
    }

    // --- TESTES PARA O MÉTODO 'DESLIGAR' ---

    @Test
    public void deveDesligarEquipamentoComSucesso() {
        // Dado
        RegistroUso registroAberto = new RegistroUso();
        registroAberto.setDataHoraInicio(LocalDateTime.now().minusMinutes(30));
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamento));
        // Simula que o equipamento está ligado (encontra um registro aberto)
        when(registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento)).thenReturn(Optional.of(registroAberto));

        // Quando
        registroUsoService.desligar(dto);

        // Então
        // Verifica que o método save foi chamado para ATUALIZAR o registro com a data de fim
        verify(registroUsoRepository, times(1)).save(registroAberto);
        assertThat(registroAberto.getDataHoraFim()).isNotNull();
    }

    @Test
    public void deveLancarExcecaoAoDesligarEquipamentoJaDesligado() {
        // Dado
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamento));
        // Simula que o equipamento está desligado (não encontra registro aberto)
        when(registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento)).thenReturn(Optional.empty());

        // Então
        assertThatThrownBy(() -> registroUsoService.desligar(dto))
            .isInstanceOf(RegraDeNegocioException.class)
            .hasMessage("Este equipamento já está desligado.");
    }

    @Test
    public void deveLancarExcecaoAoLigarEquipamentoInexistente() {
        // Dado
        when(equipamentoRepository.findById(99L)).thenReturn(Optional.empty());
        dto.setEquipamentoId(99L);

        // Então
        assertThatThrownBy(() -> registroUsoService.ligar(dto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Equipamento não encontrado.");
    }
}