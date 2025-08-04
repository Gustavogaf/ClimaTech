package ifs.climatech.controller;

import ifs.climatech.dto.EquipamentoDTO;
import ifs.climatech.service.EquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    @Autowired
    private EquipamentoService equipamentoService;

    @PostMapping
    public ResponseEntity<EquipamentoDTO> salvar(@RequestBody EquipamentoDTO dto) {
        EquipamentoDTO equipamentoSalvo = equipamentoService.salvar(dto);
        URI location = URI.create(String.format("/api/equipamentos/%s", equipamentoSalvo.getId()));
        return ResponseEntity.created(location).body(equipamentoSalvo);
    }
    
    @GetMapping
    public ResponseEntity<List<EquipamentoDTO>> listarTodos() {
        return ResponseEntity.ok(equipamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoDTO> buscarPorId(@PathVariable Long id) {
        return equipamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoDTO> atualizar(@PathVariable Long id, @RequestBody EquipamentoDTO dto) {
        EquipamentoDTO equipamentoAtualizado = equipamentoService.atualizar(id, dto);
        return ResponseEntity.ok(equipamentoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        equipamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}