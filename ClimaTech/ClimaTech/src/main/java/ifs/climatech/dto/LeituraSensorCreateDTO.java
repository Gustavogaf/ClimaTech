package ifs.climatech.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeituraSensorCreateDTO {

    private String tagEquipamento; // Sensor nos informa a TAG do equipamento
    private double amperagem;
    private double voltagem;
    private double temperatura;

}