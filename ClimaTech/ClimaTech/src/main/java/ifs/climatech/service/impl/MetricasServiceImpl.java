package ifs.climatech.service.impl;

import ifs.climatech.dto.EquipamentoStatusDTO;
import ifs.climatech.model.Equipamento;
import ifs.climatech.model.LeituraSensor;
import ifs.climatech.model.RegistroUso;
import ifs.climatech.repository.EquipamentoRepository;
import ifs.climatech.repository.LeituraSensorRepository;
import ifs.climatech.repository.RegistroUsoRepository;
import ifs.climatech.service.MetricasService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MetricasServiceImpl implements MetricasService {

    @Autowired
    private EquipamentoRepository equipamentoRepository;
    @Autowired
    private RegistroUsoRepository registroUsoRepository;
    @Autowired
    private LeituraSensorRepository leituraSensorRepository;

    @Override
    public EquipamentoStatusDTO getStatusAtual(Long equipamentoId) {
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        EquipamentoStatusDTO dto = new EquipamentoStatusDTO();
        dto.setEquipamentoId(equipamento.getId());
        dto.setTag(equipamento.getTag());

        // 1. Verifica se está ligado e calcula o tempo de uso atual
        Optional<RegistroUso> registroAberto = registroUsoRepository.findByEquipamentoAndDataHoraFimIsNull(equipamento);
        if (registroAberto.isPresent()) {
            dto.setLigado(true);
            Duration duracao = Duration.between(registroAberto.get().getDataHoraInicio(), LocalDateTime.now());
            long horas = duracao.toHours();
            long minutos = duracao.toMinutesPart();
            dto.setTempoDeUsoAtual(String.format("%dh %dm", horas, minutos));
        } else {
            dto.setLigado(false);
            dto.setTempoDeUsoAtual("Desligado");
        }

        // 2. Busca a última leitura de temperatura
        Pageable limit = PageRequest.of(0, 1); // Pede apenas o registro mais recente
        List<LeituraSensor> ultimasLeituras = leituraSensorRepository.findByEquipamentoOrderByTimestampDesc(equipamento, limit);
        if (!ultimasLeituras.isEmpty()) {
            dto.setTemperaturaAtual(ultimasLeituras.get(0).getTemperatura());
        }

        // 3. Cálculos de consumo e médias (lógica simplificada para o projeto)
        // Em um sistema real, isso seria feito com tarefas agendadas (batch) para não pesar na API.
        dto.setConsumoEnergiaUltimas24h(calcularConsumoSimplificado(equipamento));
        dto.setMediaTemperaturaMensal(24.5); // Placeholder
        dto.setMediaConsumoDiarioMensal(5.7); // Placeholder
        dto.setStatusMessage("Dados calculados com sucesso.");

        return dto;
    }
    
    // Método auxiliar para um cálculo simplificado do consumo
    private Double calcularConsumoSimplificado(Equipamento equipamento) {
        // Simulação: Pega as últimas 10 leituras e calcula o consumo médio
        Pageable limit = PageRequest.of(0, 10);
        List<LeituraSensor> leituras = leituraSensorRepository.findByEquipamentoOrderByTimestampDesc(equipamento, limit);
        if (leituras.size() < 2) return 0.0;
        
        double potenciaMediaWatts = leituras.stream()
            .mapToDouble(l -> l.getVoltagem() * l.getAmperagem())
            .average().orElse(0.0);
            
        double potenciaMediaKw = potenciaMediaWatts / 1000.0;
        
        // Simula o consumo em 1 hora com base na potência média das últimas leituras
        return potenciaMediaKw * 1; // kWh
    }
}