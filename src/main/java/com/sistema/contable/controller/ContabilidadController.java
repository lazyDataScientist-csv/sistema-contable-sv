package com.sistema.contable.controller;

import com.sistema.contable.model.Cuenta;
import com.sistema.contable.model.Transaccion;
import com.sistema.contable.service.ContabilidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contabilidad")
@CrossOrigin(origins = "*") // Permite que tu React se conecte sin bloqueos
public class ContabilidadController {

    @Autowired
    private ContabilidadService contabilidadService;

    @GetMapping("/cuentas")
    public List<Cuenta> listarCuentas() {
        return contabilidadService.obtenerCatalogo();
    }

    @GetMapping("/diario")
    public List<Transaccion> obtenerDiario(@RequestParam(required = false) Long usuarioId) {
        return contabilidadService.obtenerTransaccionesPorUsuario(usuarioId);
    }

    @PostMapping("/diario")
    public ResponseEntity<?> crearAsiento(@RequestBody Transaccion transaccion) {
        try {
            Transaccion guardada = contabilidadService.registrarAsiento(transaccion);
            return ResponseEntity.ok(guardada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/mayor")
    public List<Map<String, Object>> obtenerLibroMayor() {
        return contabilidadService.obtenerMayorizacion();
    }
}