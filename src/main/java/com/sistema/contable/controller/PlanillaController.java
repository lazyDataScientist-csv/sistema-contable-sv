package com.sistema.contable.controller;

import com.sistema.contable.service.PlanillaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/planilla")
@CrossOrigin(origins = "*")
public class PlanillaController {

    @Autowired
    private PlanillaService planillaService;

    @PostMapping("/calcular")
    public ResponseEntity<?> calcularRetenciones(@RequestBody Map<String, BigDecimal> payload) {
        try {
            BigDecimal salario = payload.get("salarioBruto");
            Map<String, BigDecimal> desglose = planillaService.calcularDeduccionesEmpleado(salario);
            return ResponseEntity.ok(desglose);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}