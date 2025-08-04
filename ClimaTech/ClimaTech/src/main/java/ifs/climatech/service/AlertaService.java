package ifs.climatech.service;

import ifs.climatech.dto.AlertaDTO;
import java.util.List;
import java.util.Optional;

public interface AlertaService {

    AlertaDTO salvar(AlertaDTO dto);
    List<AlertaDTO> listarTodos();
    Optional<AlertaDTO> buscarPorId(Long id);
    AlertaDTO atualizar(Long id, AlertaDTO dto);
    void deletar(Long id);
}