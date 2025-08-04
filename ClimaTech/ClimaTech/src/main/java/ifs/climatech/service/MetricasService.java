package ifs.climatech.service;

import ifs.climatech.dto.EquipamentoStatusDTO;

public interface MetricasService {

    EquipamentoStatusDTO getStatusAtual(Long equipamentoId);
}