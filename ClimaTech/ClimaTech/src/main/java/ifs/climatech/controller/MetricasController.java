package ifs.climatech.controller;

import ifs.climatech.dto.EquipamentoStatusDTO;
import ifs.climatech.service.MetricasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metricas")
public class MetricasController {

    @Autowired
    private MetricasService metricasService;

    @GetMapping("/equipamento/{id}")
    public ResponseEntity<EquipamentoStatusDTO> getStatusDoEquipamento(@PathVariable Long id) {
        EquipamentoStatusDTO status = metricasService.getStatusAtual(id);
        return ResponseEntity.ok(status);
    }
}