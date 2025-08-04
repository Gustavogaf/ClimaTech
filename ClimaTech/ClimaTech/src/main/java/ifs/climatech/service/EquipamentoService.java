package ifs.climatech.service;

import ifs.climatech.dto.EquipamentoDTO;
import java.util.List;
import java.util.Optional;

public interface EquipamentoService {

    EquipamentoDTO salvar(EquipamentoDTO equipamentoDTO);
    
    List<EquipamentoDTO> listarTodos();

    Optional<EquipamentoDTO> buscarPorId(Long id);

    EquipamentoDTO atualizar(Long id, EquipamentoDTO equipamentoDTO);

    void deletar(Long id);
}