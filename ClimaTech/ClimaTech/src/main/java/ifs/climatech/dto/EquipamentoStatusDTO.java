package ifs.climatech.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipamentoStatusDTO {

    // Informações básicas
    private Long equipamentoId;
    private String tag;
    private boolean ligado;

    // Métricas em tempo real (ou quase)
    private Double temperaturaAtual; // A última temperatura registrada
    private String tempoDeUsoAtual; // Ex: "2h 35m"

    // Métricas calculadas
    private Double consumoEnergiaUltimas24h; // Em kWh
    private Double mediaTemperaturaMensal;
    private Double mediaConsumoDiarioMensal; // Em kWh

    // Adicionei um campo de mensagem para feedback
    private String statusMessage;
}