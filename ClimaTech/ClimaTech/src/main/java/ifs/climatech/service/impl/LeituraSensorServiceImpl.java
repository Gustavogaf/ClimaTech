package ifs.climatech.service.impl;

import ifs.climatech.dto.LeituraSensorCreateDTO;
import ifs.climatech.model.Equipamento;
import ifs.climatech.model.LeituraSensor;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.repository.LeituraSensorRepository;
import ifs.climatech.service.LeituraSensorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class LeituraSensorServiceImpl implements LeituraSensorService {

    @Autowired
    private LeituraSensorRepository leituraSensorRepository;

    @Autowired
    private EquipamentoRepository equipamentoRepository;

    @Override
    @Transactional
    public void salvar(LeituraSensorCreateDTO dto) {
        // Encontra o equipamento pela tag única
        Equipamento equipamento = equipamentoRepository.findByTag(dto.getTagEquipamento())
            .orElseThrow(() -> new EntityNotFoundException(
                "Nenhum equipamento encontrado com a tag: " + dto.getTagEquipamento()
            ));

        LeituraSensor novaLeitura = new LeituraSensor();
        novaLeitura.setTimestamp(LocalDateTime.now()); // Pega a data e hora atuais
        novaLeitura.setAmperagem(dto.getAmperagem());
        novaLeitura.setVoltagem(dto.getVoltagem());
        novaLeitura.setTemperatura(dto.getTemperatura());
        novaLeitura.setEquipamento(equipamento);

        leituraSensorRepository.save(novaLeitura);
    }
}