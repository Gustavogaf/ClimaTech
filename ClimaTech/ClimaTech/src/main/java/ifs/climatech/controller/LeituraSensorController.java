package ifs.climatech.controller;

import ifs.climatech.dto.LeituraSensorCreateDTO;
import ifs.climatech.service.LeituraSensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leituras")
public class LeituraSensorController {

    @Autowired
    private LeituraSensorService leituraSensorService;

    @PostMapping
    public ResponseEntity<Void> receberLeitura(@RequestBody LeituraSensorCreateDTO dto) {
        leituraSensorService.salvar(dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}