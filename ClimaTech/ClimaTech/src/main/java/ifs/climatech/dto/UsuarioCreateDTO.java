package ifs.climatech.dto;

import ifs.climatech.model.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCreateDTO {
    private String nome;
    private String email;
    private String senha;
    private Usuario.TipoUsuario tipo;
}