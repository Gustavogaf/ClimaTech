package ifs.climatech.controller;

import ifs.climatech.dto.SalaDTO;
import ifs.climatech.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/salas")
public class SalaController {

    @Autowired
    private SalaService salaService;

    // Endpoint para CRIAR uma nova sala
    @PostMapping
    public ResponseEntity<SalaDTO> salvar(@RequestBody SalaDTO salaDTO) {
        SalaDTO salaSalva = salaService.salvar(salaDTO);
        URI location = URI.create(String.format("/api/salas/%s", salaSalva.getId()));
        return ResponseEntity.created(location).body(salaSalva);
    }

    // Endpoint para LISTAR TODAS as salas
    @GetMapping
    public ResponseEntity<List<SalaDTO>> listarTodas() {
        return ResponseEntity.ok(salaService.listarTodas());
    }

    // Endpoint para BUSCAR UMA sala POR ID
    @GetMapping("/{id}")
    public ResponseEntity<SalaDTO> buscarPorId(@PathVariable Long id) {
        return salaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para ATUALIZAR uma sala existente
    @PutMapping("/{id}")
    public ResponseEntity<SalaDTO> atualizar(@PathVariable Long id, @RequestBody SalaDTO salaDTO) {
        SalaDTO salaAtualizada = salaService.atualizar(id, salaDTO);
        return ResponseEntity.ok(salaAtualizada);
    }

    // Endpoint para DELETAR uma sala
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        salaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}