package ifs.climatech.dto;

import ifs.climatech.model.Pavilhao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PavilhaoDTO {

    private Long id;
    private String nome;

    // Construtor que converte uma Entidade Pavilhao para PavilhaoDTO
    public PavilhaoDTO(Pavilhao pavilhao) {
        this.id = pavilhao.getId();
        this.nome = pavilhao.getNome();
    }
}