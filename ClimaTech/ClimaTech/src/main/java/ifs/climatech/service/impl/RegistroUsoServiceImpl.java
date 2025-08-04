package ifs.climatech.service.impl;

import ifs.climatech.dto.ControleEquipamentoDTO;
import ifs.climatech.model.Equipamento;
import ifs.climatech.model.RegistroUso;
import ifs.climatech.model.Usuario;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.repository.RegistroUsoRepository;
import ifs.climatech.repository.UsuarioRepository;
import ifs.climatech.service.RegistroUsoService;
import ifs.climatech.service.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistroUsoServiceImpl implements RegistroUsoService {

    @Autowired
    private RegistroUsoRepository registroUsoRepository;
    @Autowired
    private EquipamentoRepository equipamentoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public void ligar(ControleEquipamentoDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(dto.getEquipamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        // Validação: Verifica se o equipamento já não está ligado (tem um registro de uso sem data de fim)
        registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento).ifPresent(r -> {
            throw new RegraDeNegocioException("Este equipamento já está ligado.");
        });

        RegistroUso novoRegistro = new RegistroUso();
        novoRegistro.setEquipamento(equipamento);
        novoRegistro.setUsuario(usuario);
        novoRegistro.setDataHoraInicio(LocalDateTime.now());
        // A dataHoraFim fica nula, indicando que está em uso

        registroUsoRepository.save(novoRegistro);
    }

    @Override
    @Transactional
    public void desligar(ControleEquipamentoDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(dto.getEquipamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        // Encontra o registro de uso que está "aberto" para este equipamento
        RegistroUso registroAberto = registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento)
                .orElseThrow(() -> new RegraDeNegocioException("Este equipamento já está desligado."));

        // Define a data e hora do fim e salva a atualização
        registroAberto.setDataHoraFim(LocalDateTime.now());
        registroUsoRepository.save(registroAberto);
    }
}