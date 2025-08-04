package ifs.climatech.service;

import ifs.climatech.dto.ControleEquipamentoDTO;

public interface RegistroUsoService {

    void ligar(ControleEquipamentoDTO dto);

    void desligar(ControleEquipamentoDTO dto);
}