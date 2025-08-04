package ifs.climatech.controller;

import ifs.climatech.dto.ControleEquipamentoDTO;
import ifs.climatech.service.RegistroUsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/controle")
public class ControleEquipamentoController {

    @Autowired
    private RegistroUsoService registroUsoService;

    @PostMapping("/ligar")
    public ResponseEntity<Void> ligarEquipamento(@RequestBody ControleEquipamentoDTO dto) {
        registroUsoService.ligar(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/desligar")
    public ResponseEntity<Void> desligarEquipamento(@RequestBody ControleEquipamentoDTO dto) {
        registroUsoService.desligar(dto);
        return ResponseEntity.ok().build();
    }
}