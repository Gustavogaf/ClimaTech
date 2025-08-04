package ifs.climatech.dto;

import ifs.climatech.model.Equipamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipamentoDTO {

    private Long id;
    private String tag;
    private String macAddress;
    private String ipAddress;
    private String marca;
    private String modelo;
    private Long salaId;
    private String salaNome;
    private String pavilhaoNome;

    // Construtor para converter a entidade Equipamento em DTO
    public EquipamentoDTO(Equipamento equipamento) {
        this.id = equipamento.getId();
        this.tag = equipamento.getTag();
        this.macAddress = equipamento.getMacAddress(); 
        this.ipAddress = equipamento.getIpAddress(); 
        this.marca = equipamento.getMarca();
        this.modelo = equipamento.getModelo();
        if (equipamento.getSala() != null) {
            this.salaId = equipamento.getSala().getId();
            this.salaNome = equipamento.getSala().getNome();
            if (equipamento.getSala().getPavilhao() != null) {
                this.pavilhaoNome = equipamento.getSala().getPavilhao().getNome();
            }
        }
    }
}