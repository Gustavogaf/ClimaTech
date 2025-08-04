package ifs.climatech.service.impl;

import ifs.climatech.dto.SalaDTO;
import ifs.climatech.model.Pavilhao;
import ifs.climatech.model.Sala;
import ifs.climatech.repository.PavilhaoRepository;
import ifs.climatech.repository.SalaRepository;
import ifs.climatech.service.SalaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ifs.climatech.service.exception.RegraDeNegocioException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalaServiceImpl implements SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Autowired
    private PavilhaoRepository pavilhaoRepository;

    // ... métodos salvar, listarTodas, buscarPorId ...

    @Override
    @Transactional(readOnly = true)
    public List<SalaDTO> listarTodas() {
        return salaRepository.findAll()
                .stream()
                .map(SalaDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalaDTO> buscarPorId(Long id) {
        return salaRepository.findById(id).map(SalaDTO::new);
    }



    private void validarSala(SalaDTO salaDTO, Pavilhao pavilhao) {
        boolean jaExiste = salaRepository.existsByNomeAndPavilhao(salaDTO.getNome(), pavilhao);
        if (jaExiste) {
            throw new RegraDeNegocioException(
                "Já existe uma sala com o nome '" + salaDTO.getNome() + "' neste pavilhão."
            );
        }
    }

    @Override
    @Transactional
    public SalaDTO salvar(SalaDTO salaDTO) {
        Pavilhao pavilhao = pavilhaoRepository.findById(salaDTO.getPavilhaoId())
                .orElseThrow(() -> new EntityNotFoundException("Pavilhão não encontrado..."));

        validarSala(salaDTO, pavilhao); // <-- VALIDAÇÃO AQUI

        Sala novaSala = new Sala();
        novaSala.setNome(salaDTO.getNome());
        novaSala.setPavilhao(pavilhao);

        Sala salaSalva = salaRepository.save(novaSala);
        return new SalaDTO(salaSalva);
    }

    @Override
    @Transactional
    public SalaDTO atualizar(Long id, SalaDTO salaDTO) {
        Sala salaExistente = salaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala não encontrada..."));

        // Se o nome da sala foi alterado, precisamos validar
        if (!salaExistente.getNome().equals(salaDTO.getNome())) {
             Pavilhao pavilhao = pavilhaoRepository.findById(salaDTO.getPavilhaoId())
                .orElseThrow(() -> new EntityNotFoundException("Pavilhão não encontrado..."));
            validarSala(salaDTO, pavilhao); // <-- VALIDAÇÃO AQUI
        }

        // ... resto do código do método atualizar ...
        Pavilhao pavilhao = pavilhaoRepository.findById(salaDTO.getPavilhaoId())
                .orElseThrow(() -> new EntityNotFoundException("Pavilhão não encontrado..."));

        salaExistente.setNome(salaDTO.getNome());
        salaExistente.setPavilhao(pavilhao);

        Sala salaAtualizada = salaRepository.save(salaExistente);
        return new SalaDTO(salaAtualizada);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!salaRepository.existsById(id)) {
            throw new EntityNotFoundException("Sala não encontrada com o id: " + id);
        }
        salaRepository.deleteById(id);
    }
}