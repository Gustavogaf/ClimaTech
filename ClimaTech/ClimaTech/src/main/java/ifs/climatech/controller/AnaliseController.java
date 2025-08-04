package ifs.climatech.controller;

import ifs.climatech.dto.AlertaEventoDTO;
import ifs.climatech.service.AnaliseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analise")
public class AnaliseController {

    @Autowired
    private AnaliseService analiseService;

    @PostMapping("/verificar")
    public ResponseEntity<AlertaEventoDTO> verificarEquipamento(@RequestParam Long equipamentoId) {
        return analiseService.analisarEquipamento(equipamentoId)
                .map(ResponseEntity::ok) // Se um alerta foi gerado, retorna 200 OK com o evento
                .orElse(ResponseEntity.noContent().build()); // Se não, retorna 204 No Content
    }
}