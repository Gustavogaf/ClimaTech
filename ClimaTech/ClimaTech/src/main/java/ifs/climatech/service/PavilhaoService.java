package ifs.climatech.service;

import ifs.climatech.model.Pavilhao;
import java.util.List;
import java.util.Optional;

public interface PavilhaoService {

    Pavilhao salvar(Pavilhao pavilhao);

    List<Pavilhao> listarTodos();

    Optional<Pavilhao> buscarPorId(Long id);

    Pavilhao atualizar(Long id, Pavilhao pavilhaoDetails);

    void deletar(Long id);
}
