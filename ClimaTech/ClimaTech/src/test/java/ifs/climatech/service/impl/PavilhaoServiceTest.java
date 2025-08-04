package ifs.climatech.service.impl;

import ifs.climatech.model.Pavilhao;
import ifs.climatech.repository.PavilhaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PavilhaoServiceTest {

    @Mock
    private PavilhaoRepository pavilhaoRepository;

    @InjectMocks
    private PavilhaoServiceImpl pavilhaoService;

    private Pavilhao pavilhao;

    @BeforeEach
    void setUp() {
        pavilhao = new Pavilhao();
        pavilhao.setId(1L);
        pavilhao.setNome("Pavilhão Central");
    }

    @Test
    public void quandoSalvar_entaoDeveRetornarPavilhao() {
        // Dado (Given)
        when(pavilhaoRepository.save(any(Pavilhao.class))).thenReturn(pavilhao);

        // Quando (When)
        Pavilhao pavilhaoSalvo = pavilhaoService.salvar(new Pavilhao());

        // Então (Then)
        assertThat(pavilhaoSalvo).isNotNull();
        assertThat(pavilhaoSalvo.getNome()).isEqualTo("Pavilhão Central");
    }

    @Test
    public void quandoBuscarPorId_entaoDeveRetornarPavilhao() {
        // Dado (Given)
        when(pavilhaoRepository.findById(1L)).thenReturn(Optional.of(pavilhao));

        // Quando (When)
        Optional<Pavilhao> resultado = pavilhaoService.buscarPorId(1L);

        // Então (Then)
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
    }
}