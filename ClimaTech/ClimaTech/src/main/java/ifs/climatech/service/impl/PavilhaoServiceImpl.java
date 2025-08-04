package ifs.climatech.service.impl;

import ifs.climatech.model.Pavilhao;
import ifs.climatech.repository.PavilhaoRepository;
import ifs.climatech.service.PavilhaoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PavilhaoServiceImpl implements PavilhaoService {

    @Autowired
    private PavilhaoRepository pavilhaoRepository;

    @Override
    @Transactional
    public Pavilhao salvar(Pavilhao pavilhao) {
        // Futuramente, podemos adicionar validações aqui (ex: não salvar com nome duplicado)
        return pavilhaoRepository.save(pavilhao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pavilhao> listarTodos() {
        return pavilhaoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pavilhao> buscarPorId(Long id) {
        return pavilhaoRepository.findById(id);
    }

    @Override
    @Transactional
    public Pavilhao atualizar(Long id, Pavilhao pavilhaoDetails) {
        Pavilhao pavilhao = pavilhaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pavilhão não encontrado com o id: " + id));

        pavilhao.setNome(pavilhaoDetails.getNome());
        // Se a entidade Pavilhao tivesse mais campos, eles seriam atualizados aqui.
        
        return pavilhaoRepository.save(pavilhao);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!pavilhaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pavilhão não encontrado com o id: " + id);
        }
        pavilhaoRepository.deleteById(id);
    }
}
