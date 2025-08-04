package ifs.climatech.dto;

import ifs.climatech.model.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private Usuario.TipoUsuario tipo; // Usando o Enum do modelo

    // Construtor que converte a entidade para DTO (sem a senha)
    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.tipo = usuario.getTipo();
    }
}