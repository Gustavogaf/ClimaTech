package ifs.climatech.service.impl;

import ifs.climatech.dto.AlertaDTO;
import ifs.climatech.model.Alerta;
import ifs.climatech.repository.AlertaRepository;
import ifs.climatech.service.AlertaService;
import ifs.climatech.service.exception.RegraDeNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlertaServiceImpl implements AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    @Override
    @Transactional
    public AlertaDTO salvar(AlertaDTO dto) {
        alertaRepository.findByCodigoAlerta(dto.getCodigoAlerta()).ifPresent(a -> {
            throw new RegraDeNegocioException("O código de alerta '" + dto.getCodigoAlerta() + "' já existe.");
        });

        Alerta novoAlerta = new Alerta();
        novoAlerta.setCodigoAlerta(dto.getCodigoAlerta());
        novoAlerta.setDescricao(dto.getDescricao());
        novoAlerta.setTipo(dto.getTipo());
        novoAlerta.setAtivo(dto.isAtivo());

        Alerta alertaSalvo = alertaRepository.save(novoAlerta);
        return new AlertaDTO(alertaSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertaDTO> listarTodos() {
        return alertaRepository.findAll().stream().map(AlertaDTO::new).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AlertaDTO> buscarPorId(Long id) {
        return alertaRepository.findById(id).map(AlertaDTO::new);
    }

    @Override
    @Transactional
    public AlertaDTO atualizar(Long id, AlertaDTO dto) {
        Alerta alerta = alertaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Alerta não encontrado com o id: " + id));

        if (!alerta.getCodigoAlerta().equals(dto.getCodigoAlerta())) {
            alertaRepository.findByCodigoAlerta(dto.getCodigoAlerta()).ifPresent(a -> {
                throw new RegraDeNegocioException("O código de alerta '" + dto.getCodigoAlerta() + "' já existe.");
            });
        }
        
        alerta.setCodigoAlerta(dto.getCodigoAlerta());
        alerta.setDescricao(dto.getDescricao());
        alerta.setTipo(dto.getTipo());
        alerta.setAtivo(dto.isAtivo());
        
        Alerta alertaAtualizado = alertaRepository.save(alerta);
        return new AlertaDTO(alertaAtualizado);
    }
    
    @Override
    @Transactional
    public void deletar(Long id) {
        if (!alertaRepository.existsById(id)) {
            throw new EntityNotFoundException("Alerta não encontrado com o id: " + id);
        }
        alertaRepository.deleteById(id);
    }
}