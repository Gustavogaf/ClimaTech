package ifs.climatech.service.impl;

import ifs.climatech.dto.EquipamentoDTO;
import ifs.climatech.model.Equipamento;
import ifs.climatech.model.Sala;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.repository.SalaRepository;
import ifs.climatech.service.EquipamentoService;
import ifs.climatech.service.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipamentoServiceImpl implements EquipamentoService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Autowired
    private SalaRepository salaRepository;

    private void validar(EquipamentoDTO dto) {
        // Valida Tag Única
        equipamentoRepository.findByTag(dto.getTag()).ifPresent(e -> {
            throw new RegraDeNegocioException("A tag '" + dto.getTag() + "' já está em uso.");
        });
        // Valida MAC Único
        if (equipamentoRepository.existsByMacAddress(dto.getMacAddress())) {
            throw new RegraDeNegocioException("O Endereço MAC '" + dto.getMacAddress() + "' já está cadastrado.");
        }
        // Valida IP Único
        if (equipamentoRepository.existsByIpAddress(dto.getIpAddress())) {
            throw new RegraDeNegocioException("O Endereço IP '" + dto.getIpAddress() + "' já está em uso.");
        }
    }

    @Override
    @Transactional
    public EquipamentoDTO salvar(EquipamentoDTO dto) {
        validar(dto);

        Sala sala = salaRepository.findById(dto.getSalaId())
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada com o id: " + dto.getSalaId()));

        Equipamento novoEquipamento = new Equipamento();
        novoEquipamento.setTag(dto.getTag());
        novoEquipamento.setMacAddress(dto.getMacAddress());
        novoEquipamento.setIpAddress(dto.getIpAddress());
        novoEquipamento.setMarca(dto.getMarca());
        novoEquipamento.setModelo(dto.getModelo());
        novoEquipamento.setSala(sala);

        Equipamento equipamentoSalvo = equipamentoRepository.save(novoEquipamento);
        return new EquipamentoDTO(equipamentoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EquipamentoDTO> listarTodos() {
        return equipamentoRepository.findAll().stream()
                .map(EquipamentoDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EquipamentoDTO> buscarPorId(Long id) {
        return equipamentoRepository.findById(id).map(EquipamentoDTO::new);
    }

    @Override
    @Transactional
    public EquipamentoDTO atualizar(Long id, EquipamentoDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado com o id: " + id));

        // Validações condicionais
        if (!equipamento.getTag().equals(dto.getTag())) {
            equipamentoRepository.findByTag(dto.getTag()).ifPresent(e -> {
                throw new RegraDeNegocioException("A tag '" + dto.getTag() + "' já está em uso.");
            });
        }
        if (!equipamento.getMacAddress().equals(dto.getMacAddress())) {
            if (equipamentoRepository.existsByMacAddress(dto.getMacAddress())) {
                throw new RegraDeNegocioException("O Endereço MAC '" + dto.getMacAddress() + "' já está cadastrado.");
            }
        }
        if (!equipamento.getIpAddress().equals(dto.getIpAddress())) {
            if (equipamentoRepository.existsByIpAddress(dto.getIpAddress())) {
                throw new RegraDeNegocioException("O Endereço IP '" + dto.getIpAddress() + "' já está em uso.");
            }
        }

        Sala sala = salaRepository.findById(dto.getSalaId())
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada com o id: " + dto.getSalaId()));

        equipamento.setTag(dto.getTag());
        equipamento.setMacAddress(dto.getMacAddress()); // Adicionado
        equipamento.setIpAddress(dto.getIpAddress()); // Adicionado
        equipamento.setMarca(dto.getMarca());
        equipamento.setModelo(dto.getModelo());
        equipamento.setSala(sala);

        Equipamento equipamentoAtualizado = equipamentoRepository.save(equipamento);
        return new EquipamentoDTO(equipamentoAtualizado);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!equipamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Equipamento não encontrado com o id: " + id);
        }
        equipamentoRepository.deleteById(id);
    }
}