package ifs.climatech.service.impl;

import ifs.climatech.dto.AlertaEventoDTO;
import ifs.climatech.dto.EquipamentoStatusDTO;
import ifs.climatech.model.Alerta;
import ifs.climatech.model.AlertaEvento;
import ifs.climatech.model.Equipamento;
import ifs.climatech.repository.AlertaEventoRepository;
import ifs.climatech.repository.AlertaRepository;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.service.AnaliseService;
import ifs.climatech.service.MetricasService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AnaliseServiceImpl implements AnaliseService {

    @Autowired
    private MetricasService metricasService;
    @Autowired
    private AlertaRepository alertaRepository;
    @Autowired
    private EquipamentoRepository equipamentoRepository;
    @Autowired
    private AlertaEventoRepository alertaEventoRepository;

    @Override
    @Transactional
    public Optional<AlertaEventoDTO> analisarEquipamento(Long equipamentoId) {
        EquipamentoStatusDTO status = metricasService.getStatusAtual(equipamentoId);

        // REGRA 1: Consumo de energia muito alto
        if (status.getConsumoEnergiaUltimas24h() != null && status.getConsumoEnergiaUltimas24h() > 10.0) {
            return Optional.of(dispararAlerta(equipamentoId, "CONSUMO_ALTO", 
                "Consumo elevado detectado: " + status.getConsumoEnergiaUltimas24h() + " kWh"));
        }

        // REGRA 2: Temperatura ambiente muito alta mesmo com o ar ligado
        if (status.isLigado() && status.getTemperaturaAtual() != null && status.getTemperaturaAtual() > 28.0) {
             return Optional.of(dispararAlerta(equipamentoId, "BAIXA_EFICIENCIA", 
                "Baixa eficiência detectada: Temperatura de " + status.getTemperaturaAtual() + "°C mesmo com o equipamento ligado."));
        }

        // Se nenhuma regra foi violada, não retorna nada
        return Optional.empty();
    }
    
    private AlertaEventoDTO dispararAlerta(Long equipamentoId, String codigoAlerta, String mensagem) {
        // Ignora se já houver um alerta do mesmo tipo não resolvido para este equipamento, para evitar spam.
        // Lógica a ser adicionada no futuro.
        
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado para o alerta."));
        
        Alerta alerta = alertaRepository.findByCodigoAlerta(codigoAlerta)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de alerta com código '" + codigoAlerta + "' não cadastrado."));

        AlertaEvento evento = new AlertaEvento();
        evento.setEquipamento(equipamento);
        evento.setAlerta(alerta);
        evento.setTimestamp(LocalDateTime.now());
        evento.setMensagem(mensagem);

        AlertaEvento eventoSalvo = alertaEventoRepository.save(evento);
        return new AlertaEventoDTO(eventoSalvo);
    }
}