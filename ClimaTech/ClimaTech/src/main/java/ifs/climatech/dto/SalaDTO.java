package ifs.climatech.dto;

import ifs.climatech.model.Sala;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SalaDTO {

    private Long id;
    private String nome;
    private Long pavilhaoId;
    private String pavilhaoNome;

    // Construtor que converte a entidade Sala para DTO
    public SalaDTO(Sala sala) {
        this.id = sala.getId();
        this.nome = sala.getNome();
        if (sala.getPavilhao() != null) {
            this.pavilhaoId = sala.getPavilhao().getId();
            this.pavilhaoNome = sala.getPavilhao().getNome();
        }
    }
}