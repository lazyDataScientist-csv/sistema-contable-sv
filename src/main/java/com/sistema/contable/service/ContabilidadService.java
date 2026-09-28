package com.sistema.contable.service;

import com.sistema.contable.model.Cuenta;
import com.sistema.contable.model.DetalleTransaccion;
import com.sistema.contable.model.Transaccion;
import com.sistema.contable.repository.CuentaRepository;
import com.sistema.contable.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ContabilidadService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Cuenta> obtenerCatalogo() {
        return cuentaRepository.findAll();
    }

    public List<Transaccion> obtenerLibroDiario() {
        return transaccionRepository.findAll();
    }

    @Transactional
    public Transaccion registrarAsiento(Transaccion transaccion) {
        if (transaccion.getDetalles() == null || transaccion.getDetalles().size() < 2) {
            throw new IllegalArgumentException("Un asiento contable requiere al menos dos cuentas.");
        }

        BigDecimal sumaDebe = BigDecimal.ZERO;
        BigDecimal sumaHaber = BigDecimal.ZERO;

        for (DetalleTransaccion detalle : transaccion.getDetalles()) {
            BigDecimal debe = detalle.getDebe() != null ? detalle.getDebe() : BigDecimal.ZERO;
            BigDecimal haber = detalle.getHaber() != null ? detalle.getHaber() : BigDecimal.ZERO;

            if (debe.compareTo(BigDecimal.ZERO) > 0 && haber.compareTo(BigDecimal.ZERO) > 0) {
                throw new IllegalArgumentException("Una fila no puede tener montos en Debe y Haber simultáneamente.");
            }

            sumaDebe = sumaDebe.add(debe);
            sumaHaber = sumaHaber.add(haber);
            detalle.setTransaccion(transaccion);
        }

        if (sumaDebe.compareTo(sumaHaber) != 0 || sumaDebe.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException(
                "Error de Partida Doble: El Debe ($" + sumaDebe + ") no cuadra con el Haber ($" + sumaHaber + ")."
            );
        }

        return transaccionRepository.save(transaccion);
    }

    // Motor de Mayorización (Cuentas T y Saldos)
    public List<Map<String, Object>> obtenerMayorizacion() {
        List<Transaccion> transacciones = transaccionRepository.findAll();
        Map<Integer, Map<String, Object>> mayor = new LinkedHashMap<>();

        for (Transaccion t : transacciones) {
            for (DetalleTransaccion d : t.getDetalles()) {
                Cuenta c = d.getCuenta();
                mayor.putIfAbsent(c.getId(), new HashMap<>(Map.of(
                    "codigo", c.getCodigo(),
                    "nombre", c.getNombre(),
                    "totalDebe", BigDecimal.ZERO,
                    "totalHaber", BigDecimal.ZERO
                )));

                Map<String, Object> datosCuenta = mayor.get(c.getId());
                BigDecimal debeActual = (BigDecimal) datosCuenta.get("totalDebe");
                BigDecimal haberActual = (BigDecimal) datosCuenta.get("totalHaber");

                datosCuenta.put("totalDebe", debeActual.add(d.getDebe() != null ? d.getDebe() : BigDecimal.ZERO));
                datosCuenta.put("totalHaber", haberActual.add(d.getHaber() != null ? d.getHaber() : BigDecimal.ZERO));
            }
        }

        // Calcular saldo Deudor o Acreedor por cada cuenta
        for (Map<String, Object> cuenta : mayor.values()) {
            BigDecimal debe = (BigDecimal) cuenta.get("totalDebe");
            BigDecimal haber = (BigDecimal) cuenta.get("totalHaber");
            
            if (debe.compareTo(haber) > 0) {
                cuenta.put("saldoDeudor", debe.subtract(haber));
                cuenta.put("saldoAcreedor", BigDecimal.ZERO);
            } else if (haber.compareTo(debe) > 0) {
                cuenta.put("saldoDeudor", BigDecimal.ZERO);
                cuenta.put("saldoAcreedor", haber.subtract(debe));
            } else {
                cuenta.put("saldoDeudor", BigDecimal.ZERO);
                cuenta.put("saldoAcreedor", BigDecimal.ZERO);
            }
        }

        return new ArrayList<>(mayor.values());
    }
    
    public List<Transaccion> obtenerTransaccionesPorUsuario(Long usuarioId) {
    if (usuarioId != null) {
        return transaccionRepository.findByUsuarioIdOrderByIdAsc(usuarioId);
    }
    return transaccionRepository.findAll();
}
    
}