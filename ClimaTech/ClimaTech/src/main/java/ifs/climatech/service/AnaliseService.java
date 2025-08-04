package ifs.climatech.service;

import ifs.climatech.dto.AlertaEventoDTO;
import java.util.Optional;

public interface AnaliseService {
    Optional<AlertaEventoDTO> analisarEquipamento(Long equipamentoId);
}