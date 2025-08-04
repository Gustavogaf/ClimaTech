package ifs.climatech.service;

import ifs.climatech.dto.UsuarioCreateDTO;
import ifs.climatech.dto.UsuarioDTO;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    UsuarioDTO salvar(UsuarioCreateDTO usuarioCreateDTO);

    List<UsuarioDTO> listarTodos();

    Optional<UsuarioDTO> buscarPorId(Long id);

    UsuarioDTO atualizar(Long id, UsuarioDTO usuarioDTO);

    void deletar(Long id);
}