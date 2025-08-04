package ifs.climatech.dto;

import ifs.climatech.model.Alerta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlertaDTO {

    private Long id;
    private String codigoAlerta;
    private String descricao;
    private Alerta.TipoAlerta tipo;
    private boolean ativo;

    public AlertaDTO(Alerta alerta) {
        this.id = alerta.getId();
        this.codigoAlerta = alerta.getCodigoAlerta();
        this.descricao = alerta.getDescricao();
        this.tipo = alerta.getTipo();
        this.ativo = alerta.isAtivo();
    }
}