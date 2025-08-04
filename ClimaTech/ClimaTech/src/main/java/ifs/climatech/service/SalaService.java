package ifs.climatech.service;

import ifs.climatech.dto.SalaDTO;
import java.util.List;
import java.util.Optional;

public interface SalaService {

    SalaDTO salvar(SalaDTO salaDTO);

    List<SalaDTO> listarTodas();

    Optional<SalaDTO> buscarPorId(Long id);

    
    SalaDTO atualizar(Long id, SalaDTO salaDTO);

    
    void deletar(Long id);
}