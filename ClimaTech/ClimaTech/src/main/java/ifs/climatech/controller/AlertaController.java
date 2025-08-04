package ifs.climatech.controller;

import ifs.climatech.dto.AlertaDTO;
import ifs.climatech.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @PostMapping
    public ResponseEntity<AlertaDTO> salvar(@RequestBody AlertaDTO dto) {
        AlertaDTO alertaSalvo = alertaService.salvar(dto);
        URI location = URI.create(String.format("/api/alertas/%s", alertaSalvo.getId()));
        return ResponseEntity.created(location).body(alertaSalvo);
    }

    @GetMapping
    public ResponseEntity<List<AlertaDTO>> listarTodos() {
        return ResponseEntity.ok(alertaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaDTO> buscarPorId(@PathVariable Long id) {
        return alertaService.buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaDTO> atualizar(@PathVariable Long id, @RequestBody AlertaDTO dto) {
        return ResponseEntity.ok(alertaService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        alertaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}