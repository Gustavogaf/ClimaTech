package ifs.climatech.controller;

import ifs.climatech.dto.PavilhaoDTO;
import ifs.climatech.model.Pavilhao;
import ifs.climatech.service.PavilhaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pavilhoes")
public class PavilhaoController {

    @Autowired
    private PavilhaoService pavilhaoService;

    // Endpoint para LISTAR TODOS os pavilhões
    @GetMapping
    public ResponseEntity<List<PavilhaoDTO>> listarTodos() {
        List<Pavilhao> pavilhoes = pavilhaoService.listarTodos();
        List<PavilhaoDTO> dtos = pavilhoes.stream()
                                          .map(PavilhaoDTO::new)
                                          .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Endpoint para BUSCAR UM pavilhão POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PavilhaoDTO> buscarPorId(@PathVariable Long id) {
        return pavilhaoService.buscarPorId(id)
                .map(pavilhao -> ResponseEntity.ok(new PavilhaoDTO(pavilhao)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para CRIAR um novo pavilhão
    @PostMapping
    public ResponseEntity<PavilhaoDTO> salvar(@RequestBody Pavilhao pavilhao) {
        Pavilhao novoPavilhao = pavilhaoService.salvar(pavilhao);
        URI location = URI.create(String.format("/api/pavilhoes/%s", novoPavilhao.getId()));
        return ResponseEntity.created(location).body(new PavilhaoDTO(novoPavilhao));
    }

    // Endpoint para ATUALIZAR um pavilhão existente
    @PutMapping("/{id}")
    public ResponseEntity<PavilhaoDTO> atualizar(@PathVariable Long id, @RequestBody Pavilhao pavilhaoDetails) {
        Pavilhao pavilhaoAtualizado = pavilhaoService.atualizar(id, pavilhaoDetails);
        return ResponseEntity.ok(new PavilhaoDTO(pavilhaoAtualizado));
    }

    // Endpoint para DELETAR um pavilhão
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pavilhaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}