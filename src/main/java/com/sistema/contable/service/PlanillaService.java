package com.sistema.contable.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlanillaService {

    private static final BigDecimal PORCENTAJE_ISSS = new BigDecimal("0.03");
    private static final BigDecimal TECHO_ISSS = new BigDecimal("1000.00");
    private static final BigDecimal PORCENTAJE_AFP = new BigDecimal("0.0725");

    public Map<String, BigDecimal> calcularDeduccionesEmpleado(BigDecimal salarioBruto) {
        if (salarioBruto == null || salarioBruto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El salario bruto debe ser mayor a cero.");
        }

        // 1. Cálculo del Seguro (ISSS 3% con techo de $1,000.00 -> máx $30.00)
        BigDecimal baseIsss = salarioBruto.compareTo(TECHO_ISSS) > 0 ? TECHO_ISSS : salarioBruto;
        BigDecimal descuentoIsss = baseIsss.multiply(PORCENTAJE_ISSS).setScale(2, RoundingMode.HALF_UP);

        // 2. Cálculo de AFP (7.25%)
        BigDecimal descuentoAfp = salarioBruto.multiply(PORCENTAJE_AFP).setScale(2, RoundingMode.HALF_UP);

        // 3. Total retenido y Salario Líquido
        BigDecimal totalRetenciones = descuentoIsss.add(descuentoAfp);
        BigDecimal salarioLiquido = salarioBruto.subtract(totalRetenciones);

        Map<String, BigDecimal> resultado = new HashMap<>();
        resultado.put("salarioBruto", salarioBruto.setScale(2, RoundingMode.HALF_UP));
        resultado.put("isss", descuentoIsss);
        resultado.put("afp", descuentoAfp);
        resultado.put("totalRetenciones", totalRetenciones);
        resultado.put("salarioLiquido", salarioLiquido);

        return resultado;
    }
}