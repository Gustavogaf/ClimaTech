package ifs.climatech.service.impl;

import ifs.climatech.dto.UsuarioCreateDTO;
import ifs.climatech.dto.UsuarioDTO;
import ifs.climatech.model.Usuario;
import ifs.climatech.repository.UsuarioRepository;
import ifs.climatech.service.UsuarioService;
import ifs.climatech.service.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UsuarioDTO salvar(UsuarioCreateDTO dto) {
        usuarioRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new RegraDeNegocioException("O e-mail '" + dto.getEmail() + "' já está em uso.");
        });

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setSenha(dto.getSenha()); // Provisório: Em um projeto real, criptografaríamos a senha aqui.
        novoUsuario.setTipo(dto.getTipo());

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);
        return new UsuarioDTO(usuarioSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(UsuarioDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> buscarPorId(Long id) {
        return usuarioRepository.findById(id).map(UsuarioDTO::new);
    }

    @Override
    @Transactional
    public UsuarioDTO atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o id: " + id));

        // Valida se o novo e-mail já não está em uso por OUTRO usuário
        if (!usuario.getEmail().equals(dto.getEmail())) {
            usuarioRepository.findByEmail(dto.getEmail()).ifPresent(u -> {
                throw new RegraDeNegocioException("O e-mail '" + dto.getEmail() + "' já está em uso.");
            });
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTipo(dto.getTipo());
        // A senha não é atualizada por este método por segurança.

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return new UsuarioDTO(usuarioAtualizado);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado com o id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}