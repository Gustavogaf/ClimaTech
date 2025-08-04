package ifs.climatech.dto;

import ifs.climatech.model.AlertaEvento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AlertaEventoDTO {
    private Long id;
    private String codigoAlerta;
    private String descricaoAlerta;
    private String tagEquipamento;
    private LocalDateTime timestamp;
    private String mensagem;
    private boolean resolvido;

    public AlertaEventoDTO(AlertaEvento evento) {
        this.id = evento.getId();
        this.timestamp = evento.getTimestamp();
        this.mensagem = evento.getMensagem();
        this.resolvido = evento.isResolvido();

        if (evento.getAlerta() != null) {
            this.codigoAlerta = evento.getAlerta().getCodigoAlerta();
            this.descricaoAlerta = evento.getAlerta().getDescricao();
        }
        if (evento.getEquipamento() != null) {
            this.tagEquipamento = evento.getEquipamento().getTag();
        }
    }
}